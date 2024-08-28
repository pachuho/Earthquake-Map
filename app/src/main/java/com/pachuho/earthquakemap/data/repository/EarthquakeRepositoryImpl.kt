package com.pachuho.earthquakemap.data.repository

import android.accounts.NetworkErrorException
import com.pachuho.earthquakemap.data.db.EarthquakeDao
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.data.remote.EarthquakeService
import com.pachuho.earthquakemap.data.utils.NetworkFailureException
import com.pachuho.earthquakemap.ui.util.UiState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(
    private val earthquakeService: EarthquakeService,
    private val earthquakeDao: EarthquakeDao
) : EarthquakeRepository {

    override fun getEarthquakes(oauthKey: String) = flow {
        if (earthquakeDao.getDataCount() > 0) { // 데이터가 있는 경우
            earthquakeDao.getLatestEarthquake()?.let { earthquake ->
                // 최신 데이터인 경우
                if (earthquake.EQ_ID == earthquakeService.fetchEarthquakes(oauthKey, 1, 1)
                        .body()?.tbEqkKenvinfo?.rows?.first()?.EQ_ID
                ) {
                    emit(earthquakeDao.getEarthquakes())
                    return@flow
                }
            }
        }
        // 다운로드 시작
        emit(emptyList())

        var isDownloading = true
        var index = 1
        val earthquakes = mutableListOf<Earthquake>()
        Timber.d("getAllEarthquakes, start")
        while (isDownloading) {
            try {
                val response = earthquakeService.fetchEarthquakes(oauthKey, index, index + 99)
                index += 100

                when (response.isSuccessful) {
                    true -> {
                        Timber.d("getAllEarthquakes, isSuccessful")
                        response.body()?.tbEqkKenvinfo?.rows?.let {
                            // todo

                            earthquakes.addAll(it)
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
                    emit(earthquakes)
                } else {
                    Timber.d("404, error: $e")
                    throw NetworkErrorException("네트워크가 연결되어있는지 확인해주세요.")
                }
            }
        }
    }
}