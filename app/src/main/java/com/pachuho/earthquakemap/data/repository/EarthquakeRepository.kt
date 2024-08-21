package com.pachuho.earthquakemap.data.repository

import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.util.UiState
import kotlinx.coroutines.flow.Flow

interface EarthquakeRepository {
    fun getEarthquakes(oauthKey: String): Flow<List<Earthquake>>
}