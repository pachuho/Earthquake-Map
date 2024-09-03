package com.pachuho.earthquakemap.ui.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.data.datasource.SettingResult
import com.pachuho.earthquakemap.data.datasource.Settings
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.screen.map.components.ActionIcons
import com.pachuho.earthquakemap.ui.screen.map.components.MapActionIcons
import com.pachuho.earthquakemap.ui.screen.map.components.MapInfo
import com.pachuho.earthquakemap.ui.screen.map.components.MapMarker
import com.pachuho.earthquakemap.ui.screen.map.components.MapSettings
import com.pachuho.earthquakemap.ui.screen.map.components.getShowingMarker
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType.Companion.find
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.successOrNull
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun MapRoute(
    viewModel: MapViewModel = hiltViewModel(),
    onSnackBar: (message: String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val settingFlow = viewModel.settingsFlow.collectAsStateWithLifecycle().value
    val seoul by remember { mutableStateOf(LatLng(37.532600, 127.024612)) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition(seoul, 7.0) }

    LaunchedEffect(true) {
        viewModel.errorFlow.collectLatest { throwable ->
            Timber.i("Error, ${throwable.message}")
            throwable.message?.let { onSnackBar(it) }
        }
    }

    when (uiState.value) {
        is UiState.Loading -> {
            Timber.i("Loading")
        }

        is UiState.Downloading -> {
            Timber.i("Downloading")

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimationLoader()
            }
        }

        is UiState.Success -> {
            Timber.i("Success")
            uiState.value.successOrNull()?.let { earthquakes ->
                EarthquakeMap(cameraPositionState, earthquakes, settingFlow)
                MapScreenComponents(settingFlow) { result ->
                    when (result) {
                        is SettingResult.DateType -> SettingResult.DateType(result.dateType)
                        is SettingResult.DateStartAndEnd -> SettingResult.DateStartAndEnd(
                            result.start,
                            result.end
                        )

                        is SettingResult.MagnitudeRange -> SettingResult.MagnitudeRange(
                            result.start,
                            result.end
                        )

                        is SettingResult.MapType -> SettingResult.MapType(result.mapType)
                    }.let {
                        viewModel.updateSetting(it)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimationLoader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        contentScale = ContentScale.FillHeight
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreenComponents(
    settings: Settings,
    onSettingResult: (SettingResult) -> Unit
) {
    var isShowingInfo by remember { mutableStateOf(false) }
    var isShowingSettings by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapActionIcons(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) { iconGroup ->
            when (iconGroup) {
                is ActionIcons.Info -> isShowingInfo = !isShowingInfo
                is ActionIcons.List -> {}
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
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun EarthquakeMap(
    cameraPositionState: CameraPositionState,
    earthquakes: List<Earthquake>,
    settings: Settings
) {
    Timber.e("EarthquakeMap, earthquakes size: ${earthquakes.size}")
    var isShowingMarker by remember { mutableStateOf(false) }

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(settings.mapType.find()),
        uiSettings = MapUiSettings(
            isLocationButtonEnabled = true,
        ),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            Timber.e("onMapLoaded")
            isShowingMarker = true
        }
    ) {
        if (isShowingMarker && earthquakes.isNotEmpty()) {
            MapMarkers(earthquakes, settings)
        }
    }
}

@Composable
private fun MapMarkers(
    earthquakes: List<Earthquake>,
    settings: Settings
) {
    Timber.e("MapMarkers")

    earthquakes
        .filter {
            getShowingMarker(
                settings.dateType,
                settings.dateStart,
                settings.dateEnd,
                it.ORIGIN_TIME
            )
        }
        .forEach { earthquake ->
            MapMarker(earthquake, settings.magStart..settings.magEnd)
        }
}