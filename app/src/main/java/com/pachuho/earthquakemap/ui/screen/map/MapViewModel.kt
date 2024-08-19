package com.pachuho.earthquakemap.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pachuho.earthquakemap.BuildConfig
import com.pachuho.earthquakemap.data.repository.EarthquakeRepository
import com.pachuho.earthquakemap.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    earthquakeRepository: EarthquakeRepository
) : ViewModel() {
    val uiState = earthquakeRepository.getEarthquakes(BuildConfig.EARTHQUAKE_OAUTH_KEY)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UiState.Loading
        )

}