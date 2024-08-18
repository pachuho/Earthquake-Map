package com.pachuho.earthquakemap.data.repository

import com.pachuho.earthquakemap.BuildConfig
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.data.remote.EarthquakeService
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.lol.data.utils.EmptyBodyException
import com.pachuho.lol.data.utils.NetworkFailureException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(
    private val earthquakeService: EarthquakeService
) : EarthquakeRepository {

    override fun getEarthquakes(oauthKey: String): Flow<UiState<List<Earthquake>>> =
        flow<UiState<List<Earthquake>>> {
//        val earthquakeInDB = earthquakeDao.getEarthquakes()
//        if (earthquakeInDB.isNotEmpty()) {
//            emit(UiState.Success(earthquakeInDB))
//            return@flow
//        }

            val response = earthquakeService.fetchEarthquakes(oauthKey)
            if (response.isSuccessful) {
                val earthquakes: List<Earthquake> =
                    response.body()?.tbEqkKenvinfo?.rows
                        ?: throw EmptyBodyException("[${response.code()}] - ${response.raw()}")
//            earthquakeDao.insertEarthQuakes(earthquakes)
                emit(UiState.Success(earthquakes))
            } else {
                throw NetworkFailureException("[${response.code()}] - ${response.raw()}")
            }
        }.catch { emit(UiState.Error(it)) }
}