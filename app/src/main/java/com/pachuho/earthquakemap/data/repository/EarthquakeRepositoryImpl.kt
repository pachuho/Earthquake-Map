package com.pachuho.earthquakemap.data.repository

import android.accounts.NetworkErrorException
import com.pachuho.earthquakemap.EarthquakeApplication
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.data.datasource.UpdatePreferencesDataSource
import com.pachuho.earthquakemap.data.db.EarthquakeDao
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.data.remote.EarthquakeService
import com.pachuho.earthquakemap.data.utils.NetworkFailureException
import com.pachuho.earthquakemap.data.utils.isToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(
    private val earthquakeService: EarthquakeService,
    private val earthquakeDao: EarthquakeDao,
    private val updatePreferencesDataSource: UpdatePreferencesDataSource,
    private val application: EarthquakeApplication
) : EarthquakeRepository {

    override fun getEarthquakes(oauthKey: String) = flow {
        if (earthquakeDao.getDataCount() <= 0) {
            emit(emptyList())
            downloadNewEarthquakes(oauthKey)
            return@flow
        }
        Timber.i("data exists")

        if (isDataUpdatedToday()) {
            emit(earthquakeDao.getEarthquakes())
            return@flow
        }

        val earthquake = earthquakeDao.getLatestEarthquake()
        if (earthquake != null && isLatestData(oauthKey, earthquake.EQ_ID)) {
            Timber.i("data is latest")
            emit(earthquakeDao.getEarthquakes())
            updateCheckDate()
            return@flow
        }

        emit(emptyList())
        downloadNewEarthquakes(oauthKey)
    }

    private fun isDataUpdatedToday(): Boolean {
        return updatePreferencesDataSource.lastUpdateDateTime?.isToday() == true
    }

    private suspend fun isLatestData(oauthKey: String, localEqId: String): Boolean {
        val latestEarthquake = withContext(Dispatchers.IO) {
            earthquakeService.fetchEarthquakes(oauthKey, 1, 1)
                .body()?.tbEqkKenvinfo?.rows?.first()
        }
        return latestEarthquake?.EQ_ID == localEqId
    }

    private suspend fun FlowCollector<List<Earthquake>>.downloadNewEarthquakes(oauthKey: String) {
        Timber.i("data download start")

        var index = 1
        val earthquakes = mutableListOf<Earthquake>()
        val lastDataId = earthquakeDao.getLatestEarthquake()?.EQ_ID
        var isDownloading = true

        while (isDownloading) {
            try {
                val response = earthquakeService.fetchEarthquakes(oauthKey, index, index + 99)
                index += 100

                if (response.isSuccessful) {
                    response.body()?.tbEqkKenvinfo?.rows?.let {
                        earthquakes.addAll(it)

                        // 중복 데이터가 있는 경우 다운로드 중지
                        if (it.any { it.EQ_ID == lastDataId }) {
                            withContext(Dispatchers.IO) {
                                earthquakeDao.insertEarthquakes(earthquakes)
                            }
                            updateCheckDate()
                            emit(earthquakes)
                            isDownloading = false
                        }
                    } ?: throw NetworkFailureException(application.getString(R.string.network_error_description_response_empty))
                } else {
                    throw NetworkFailureException(application.getString(R.string.network_error_description_resonpse_fail))
                }
            } catch (e: Exception) {
                handleDownloadException(e, earthquakes)
                isDownloading = false
            }
        }
    }

    private suspend fun FlowCollector<List<Earthquake>>.handleDownloadException(
        e: Exception,
        earthquakes: List<Earthquake>
    ) {
        Timber.e("Error during download: $e")
        if (earthquakes.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                earthquakeDao.insertEarthquakes(earthquakes)
            }
            updateCheckDate()
            emit(earthquakes)
        } else {
            throw NetworkErrorException(application.getString(R.string.network_error_description_unknown))
        }
    }

    private fun updateCheckDate() {
        LocalDateTime.now().run {
            updatePreferencesDataSource.lastUpdateDateTime =
                (year.toString() + monthValue.toString().padStart(2, '0') + dayOfMonth.toString()
                    .padStart(2, '0')).toInt()
        }
    }
}