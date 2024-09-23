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
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import java.time.LocalDateTime
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(
    private val earthquakeService: EarthquakeService,
    private val earthquakeDao: EarthquakeDao,
    private val updatePreferencesDataSource: UpdatePreferencesDataSource,
    private val application: EarthquakeApplication
) : EarthquakeRepository {

    override fun getEarthquakes(oauthKey: String) = flow {
        if (earthquakeDao.getDataCount() > 0) { // 데이터가 있는 경우
            Timber.i("data is exist")
            updatePreferencesDataSource.lastUpdateDateTime?.let {
                if (it.isToday()) { // 오늘 업데이트 된 경우
                    Timber.i("data is updated today")
                    emit(earthquakeDao.getEarthquakes())
                    return@flow
                } else {
                    Timber.i("data isn't updated today")
                    earthquakeDao.getLatestEarthquake()?.let { earthquake ->
                        // 업데이트 날짜는 다르지만 최신 데이터를 가지고 있는 경우
                        if (earthquake.EQ_ID == earthquakeService.fetchEarthquakes(oauthKey, 1, 1)
                                .body()?.tbEqkKenvinfo?.rows?.first()?.EQ_ID
                        ) {
                            Timber.i("data is latest")
                            emit(earthquakeDao.getEarthquakes())
                            updateCheckDate()
                            return@flow
                        }
                    }
                }
            }
        }

        emit(emptyList())
        Timber.i("data download start")

        var isDownloading = true
        var index = 1
        val earthquakes = mutableListOf<Earthquake>()
        val lastDataId = earthquakeDao.getLatestEarthquake()?.EQ_ID

        while (isDownloading) {
            try {
                val response = earthquakeService.fetchEarthquakes(oauthKey, index, index + 99)
                index += 100

                when (response.isSuccessful) {
                    true -> {
                        Timber.d("getAllEarthquakes, isSuccessful")
                        response.body()?.tbEqkKenvinfo?.rows?.let {
                            Timber.d("add data, isSuccessful")
                            earthquakes.addAll(it)

                            // 중복 데이터가 있는 경우 다운로드 중지
                            val ids = it.map { it.EQ_ID }
                            if(ids.contains(lastDataId)) {
                                earthquakeDao.insertEarthquakes(earthquakes)
                                updateCheckDate()
                                Timber.i("data download finish")
                                emit(earthquakes)
                            }

                        } ?: run {
                            Timber.d("getAllEarthquakes, empty.." + "[${response.code()}] - ${response.raw()}")
                            throw NetworkFailureException(application.getString(R.string.network_error_description_response_empty))
                        }
                    }

                    false -> {
                        Timber.e("response failure" + "[${response.code()}] - ${response.raw()}")
                        throw NetworkFailureException(application.getString(R.string.network_error_description_resonpse_fail))
                    }
                }
            } catch (e: JsonDataException) {
                Timber.d("getAllEarthquakes, catch: $e")
                isDownloading = false

                if (earthquakes.isNotEmpty()) {
                    earthquakeDao.insertEarthquakes(earthquakes)
                    updateCheckDate()
                    Timber.i("data download finish")
                    emit(earthquakes)
                } else {
                    Timber.d("getAllEarthquakes, catch: $e")
                    throw NetworkErrorException(application.getString(R.string.network_error_description_unknown))
                }
            } catch (e: SocketTimeoutException) {
                Timber.d("getAllEarthquakes, catch: $e")
                throw NetworkErrorException(application.getString(R.string.network_error_description_socket_timeout))
            }
            catch (e: Exception) {
                Timber.d("getAllEarthquakes, catch: $e")
                throw NetworkErrorException(application.getString(R.string.network_error_description_exception))
            }
        }
    }

    private fun updateCheckDate() {
        LocalDateTime.now().run {
            updatePreferencesDataSource.lastUpdateDateTime = (year.toString() + monthValue.toString().padStart(2, '0') + dayOfMonth.toString().padStart(2, '0')).toInt()
        }
    }
}