package com.pachuho.earthquakemap.ui.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.pachuho.earthquakemap.data.datasource.SettingResult
import com.pachuho.earthquakemap.data.datasource.Settings
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.screen.map.components.ActionIcons
import com.pachuho.earthquakemap.ui.screen.map.components.MapActionIcons
import com.pachuho.earthquakemap.ui.screen.map.components.MapInfo
import com.pachuho.earthquakemap.ui.screen.map.components.MapMarker
import com.pachuho.earthquakemap.ui.screen.map.components.MapSettings
import com.pachuho.earthquakemap.ui.screen.map.components.MarkerInfo
import com.pachuho.earthquakemap.ui.screen.map.components.getShowingMarker
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType.Companion.find
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.showToast
import com.pachuho.earthquakemap.ui.util.successOrNull
import timber.log.Timber

@Composable
fun MapRoute(
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val settingFlow = viewModel.settingsFlow.collectAsStateWithLifecycle().value

    Timber.e("setting: $settingFlow")
    MapScreen(uiState.value, settingFlow) { result ->
        when (result) {
            is SettingResult.DateType -> SettingResult.DateType(result.dateType)
            is SettingResult.DateStartAndEnd -> SettingResult.DateStartAndEnd(result.start, result.end)
            is SettingResult.MagnitudeRange -> SettingResult.MagnitudeRange(result.start, result.end)
            is SettingResult.MapType -> SettingResult.MapType(result.mapType)
        }.let {
            viewModel.updateSetting(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    uiState: UiState<List<Earthquake>>,
    settings: Settings,
    onSettingResult: (SettingResult) -> Unit
) {
    val context = LocalContext.current
    var isShowingInfo by remember { mutableStateOf(false) }
    var isShowingSettings by remember { mutableStateOf(false) }
    var currentShowingMarkerInfo by remember { mutableStateOf<Earthquake?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        EarthquakeMap(uiState, settings) { earthquake ->
            currentShowingMarkerInfo = earthquake
        }

        MapActionIcons(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) { iconGroup ->
            when (iconGroup) {
                is ActionIcons.Info -> isShowingInfo = !isShowingInfo
                is ActionIcons.Location -> {}
                is ActionIcons.Settings -> isShowingSettings = !isShowingSettings
            }

        }
    }

    if (isShowingInfo) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                isShowingInfo = false
            }
        ) {
            MapInfo()
        }
    }

    if (isShowingSettings) {
        uiState.successOrNull()?.let { earthquakes ->
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = {
                    isShowingSettings = false
                }
            ) {
                MapSettings(
                    minDateTime = settings.dateMin,
                    startDateTime = settings.dateStart,
                    endDateTime = settings.dateEnd,
                    currentMagSliderPosition = settings.magStart..settings.magEnd,
                    currentDateType = settings.dateType,
                    currentMapType = settings.mapType
                ) { settingResult ->
                    onSettingResult(settingResult)
                }

            }
        } ?: run {
            context.showToast("잠시 후 시도해 주세요.")
        }
    }

    currentShowingMarkerInfo?.let {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                currentShowingMarkerInfo = null
            }
        ) {
            MarkerInfo(it)
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun EarthquakeMap(
    uiState: UiState<List<Earthquake>>,
    settings: Settings,
    onMarkerClick: (Earthquake) -> Unit
) {
    Timber.e("EarthquakeMap")

    val seoul = LatLng(37.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(seoul, 7.0)
    }

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(mapType = settings.mapType.find()),
        cameraPositionState = cameraPositionState
    ) {
        when (uiState) {
            is UiState.Loading -> {
                Timber.e("MapScreen: Loading")
            }

            is UiState.Success -> {
                Timber.e("MapScreen: Success")
                uiState.successOrNull()?.let { earthquakes ->
                        MapMarkers(earthquakes, settings) { earthquake ->
                            onMarkerClick(earthquake)
                        }
                }
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
private fun MapMarkers(
    earthquakes: List<Earthquake>,
    settings: Settings,
    onMarkerClick: (Earthquake) -> Unit
) {
    Timber.e("MapMarkers")

    earthquakes
        .filter { getShowingMarker(settings.dateType, settings.dateStart, settings.dateEnd, it.ORIGIN_TIME) }
        .forEach { earthquake ->

            MapMarker(earthquake, settings.magStart..settings.magEnd) {
                onMarkerClick(it)
            }
        }
}