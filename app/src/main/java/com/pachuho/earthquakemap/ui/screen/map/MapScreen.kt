package com.pachuho.earthquakemap.ui.screen.map

import android.accounts.NetworkErrorException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.screen.map.components.ActionIcons
import com.pachuho.earthquakemap.ui.screen.map.components.MapActionIcons
import com.pachuho.earthquakemap.ui.screen.map.components.MapInfo
import com.pachuho.earthquakemap.ui.screen.map.components.MapMarker
import com.pachuho.earthquakemap.ui.screen.map.components.MapSettings
import com.pachuho.earthquakemap.ui.screen.map.components.MarkerInfo
import com.pachuho.earthquakemap.ui.screen.map.components.SettingMapType
import com.pachuho.earthquakemap.ui.screen.map.components.SettingMapType.Companion.find
import com.pachuho.earthquakemap.ui.screen.map.components.SettingsResult
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.successOrNull
import timber.log.Timber

@Composable
fun MapRoute(
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MapScreen(uiState.value)
    uiState.value.successOrNull()?.let {
    }
}

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(uiState: UiState<List<Earthquake>>) {
    val context = LocalContext.current
    var isShowingInfo by remember { mutableStateOf(false) }
    var isShowingSettings by remember { mutableStateOf(false) }
    var currentShowingMarkerInfo by remember { mutableStateOf<Earthquake?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var magSliderPosition by remember { mutableStateOf(1.0f..10.0f) }
    var currentMapType by remember { mutableStateOf(SettingMapType.Basic) }

    val seoul = LatLng(37.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(seoul, 7.0)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(mapType = currentMapType.find()),
            cameraPositionState = cameraPositionState
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    Timber.e("MapScreen: Loading")
                }

                is UiState.Success -> {
                    uiState.successOrNull()?.let { earthquakes ->
                        Timber.e("MapScreen:, Success")
                        earthquakes
                            .forEach { earthquake ->
                                MapMarker(earthquake, magSliderPosition) {
                                    currentShowingMarkerInfo = it
                                }
                            }
                    }
                }

                is UiState.Error -> {}
            }
        }

        MapActionIcons(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) { iconGroup ->
            when(iconGroup) {
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
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                isShowingSettings = false
            }
        ) {
            MapSettings(magSliderPosition, currentMapType) { settingResult ->
                when(settingResult) {
                    is SettingsResult.Magnitude -> {
                        magSliderPosition = settingResult.value
                    }
                    is SettingsResult.MapType -> {
                        currentMapType = settingResult.mapType
                    }
                }
            }
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

class MapScreenPreviewParameterProvider :
    PreviewParameterProvider<UiState<List<Earthquake>>> {
    override val values = sequenceOf(
        UiState.Loading,
        UiState.Success(emptyList<Earthquake>()),
        UiState.Error(NetworkErrorException("network Error 404"))
    )
}

//@Composable
//@Preview
//private fun MapScreenPreview(
//    @PreviewParameter(MapScreenPreviewParameterProvider::class) uiState: UiState<List<Earthquake>>
//) {
//    MapScreen(uiState = uiState)
//}