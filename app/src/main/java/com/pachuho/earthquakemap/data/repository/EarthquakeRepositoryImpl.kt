package com.pachuho.earthquakemap.data.repository

import android.accounts.NetworkErrorException
import com.pachuho.earthquakemap.data.db.EarthquakeDao
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.data.remote.EarthquakeService
import com.pachuho.earthquakemap.data.utils.NetworkFailureException
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

        var isDownloading = true
        var index = 1
        val earthquakes = mutableListOf<Earthquake>()
        Timber.d("getAllEarthquakes, start")
        while (isDownloading) {
            try {
                val response = earthquakeService.fetchEarthquakes(oauthKey, index, index + 999)
                index += 1000

                when (response.isSuccessful) {
                    true -> {
                        Timber.d("getAllEarthquakes, isSuccessful")
                        response.body()?.tbEqkKenvinfo?.rows?.let {
                            earthquakes.addAll(it)
                        } ?: run {
                            Timber.d("getAllEarthquakes, empty")
                            throw NetworkFailureException("[${response.code()}] - ${response.raw()}")
                        }
                    }

                    false -> {
                        Timber.e("response failure")
                        throw NetworkFailureException("[${response.code()}] - ${response.raw()}")
                    }
                }
            } catch (e: Exception) {
                Timber.d("getAllEarthquakes, catch: $e")
                isDownloading = false

                if (earthquakes.isNotEmpty()) {
                    earthquakeDao.insertEarthquakes(earthquakes)
                    emit(earthquakes)
                } else {
                    throw NetworkErrorException("[404] - ${e.message}")
                }
            }
        }
    }
}