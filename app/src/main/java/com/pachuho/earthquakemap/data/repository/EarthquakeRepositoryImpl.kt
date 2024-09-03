package com.pachuho.earthquakemap.data.repository

import android.accounts.NetworkErrorException
import com.pachuho.earthquakemap.data.datasource.UpdatePreferencesDataSource
import com.pachuho.earthquakemap.data.db.EarthquakeDao
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.data.remote.EarthquakeService
import com.pachuho.earthquakemap.data.utils.NetworkFailureException
import com.pachuho.earthquakemap.data.utils.isToday
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(
    private val earthquakeService: EarthquakeService,
    private val earthquakeDao: EarthquakeDao,
    private val updatePreferencesDataSource: UpdatePreferencesDataSource
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
                            throw NetworkFailureException("서울시 지진안전포털에서 데이터를 불러오지 못했어요.\n잠시 후 다시 시도해 주세요.")
                        }
                    }

                    false -> {
                        Timber.e("response failure" + "[${response.code()}] - ${response.raw()}")
                        throw NetworkFailureException("서울시 지진안전포털에서 데이터를 불러오지 못했어요.\n잠시 후 다시 시도해 주세요..")
                    }
                }
            } catch (e: Exception) {
                Timber.d("getAllEarthquakes, catch: $e")
                isDownloading = false

                if (earthquakes.isNotEmpty()) {
                    earthquakeDao.insertEarthquakes(earthquakes)
                    updateCheckDate()
                    Timber.i("data download finish")
                    emit(earthquakes)
                } else {
                    Timber.e("404, error: $e")
                    throw NetworkErrorException("네트워크가 연결되어있는지 확인해주세요.")
                }
            }
        }
    }

    private fun updateCheckDate() {
        LocalDateTime.now().run {
            updatePreferencesDataSource.lastUpdateDateTime = (year.toString() + monthValue.toString().padStart(2, '0') + dayOfMonth.toString().padStart(2, '0')).toInt()
        }
    }
}