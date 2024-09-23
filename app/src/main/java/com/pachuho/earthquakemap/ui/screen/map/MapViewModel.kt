package com.pachuho.earthquakemap.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pachuho.earthquakemap.BuildConfig
import com.pachuho.earthquakemap.data.datasource.SettingResult
import com.pachuho.earthquakemap.data.datasource.SettingPreferencesDataSource
import com.pachuho.earthquakemap.data.datasource.Settings
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.data.repository.EarthquakeRepository
import com.pachuho.earthquakemap.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val earthquakeRepository: EarthquakeRepository,
    private val settingPreferencesDataSource: SettingPreferencesDataSource
) : ViewModel() {
    private var _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<List<Earthquake>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Earthquake>>> = _uiState.asStateFlow()

    val settingsFlow: StateFlow<Settings> = settingPreferencesDataSource.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = Settings()
        )

    init {
        fetchEarthquakes()
    }

    fun fetchEarthquakes() = viewModelScope.launch {
        earthquakeRepository.getEarthquakes(BuildConfig.EARTHQUAKE_OAUTH_KEY)
            .map {
                when (it.isEmpty()) {
                    true -> UiState.Downloading
                    false -> UiState.Success(it)
                }
            }
            .catch { throwable ->
                _errorFlow.emit(throwable)
            }
            .collect { uiState ->
                _uiState.value = uiState
            }
    }

    fun updateSetting(action: SettingResult) = settingPreferencesDataSource.updateSetting(action)
}