package com.pachuho.earthquakemap.ui.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.naver.maps.map.compose.DisposableMapEffect
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
import com.pachuho.earthquakemap.ui.screen.map.components.MapSettings
import com.pachuho.earthquakemap.ui.screen.map.components.MarkerInfo
import com.pachuho.earthquakemap.ui.screen.map.components.filterMarkers
import com.pachuho.earthquakemap.ui.screen.map.components.getCustomMaker
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType.Companion.find
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.successOrNull
import kotlinx.coroutines.flow.collectLatest
import ted.gun0912.clustering.naver.TedNaverClustering
import timber.log.Timber

@Composable
fun MapRoute(
    viewModel: MapViewModel = hiltViewModel(),
    onSnackBar: (message: String) -> Unit,
    onNavigateToTable: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val settingFlow = viewModel.settingsFlow.collectAsStateWithLifecycle().value
    var isShowTryButton by remember { mutableStateOf(false) }
    val seoul by remember { mutableStateOf(LatLng(37.532600, 127.024612)) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition(seoul, 7.0) }

    LaunchedEffect(true) {
        viewModel.errorFlow.collectLatest { throwable ->
            Timber.i("Error, ${throwable.message}")
            throwable.message?.let { onSnackBar(it) }
            isShowTryButton = true
        }
    }

    when (uiState.value) {
        is UiState.Loading -> {
            Timber.i("Loading")
        }

        is UiState.Downloading -> {
            Timber.i("Downloading")

            AnimationLoader(isShowTryButton)
        }

        is UiState.Success -> {
            Timber.i("Success")
            uiState.value.successOrNull()?.let { earthquakes ->
                val filteredEarthquakes = filterMarkers(earthquakes, settingFlow)
                EarthquakeMap(cameraPositionState, filteredEarthquakes, settingFlow.mapType)
                MapScreenComponents(
                    settings = settingFlow,
                    onNavigateToTable = onNavigateToTable
                ) { result ->
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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (isShowTryButton) {
            Button(
                modifier = Modifier.padding(30.dp),
                onClick = {
                    isShowTryButton = false
                    viewModel.fetchEarthquakes()
                }) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun AnimationLoader(isShowTryButton: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        val isLottieLoaded = composition != null

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.align(Alignment.Center)
        )

        if (isLottieLoaded && !isShowTryButton) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = stringResource(R.string.getting_earthquake_data),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                SpacerLarge()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreenComponents(
    settings: Settings,
    onNavigateToTable: () -> Unit,
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
                is ActionIcons.List -> onNavigateToTable()
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

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EarthquakeMap(
    cameraPositionState: CameraPositionState,
    earthquakes: List<Earthquake>,
    mapType: SettingMapType
) {
    Timber.e("EarthquakeMap, earthquakes size: ${earthquakes.size}")
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var clusterManager by remember { mutableStateOf<TedNaverClustering<Earthquake>?>(null) }
    var isShowingMarker by remember { mutableStateOf(false) }
    var currentShowingMarkerInfo by remember { mutableStateOf<Earthquake?>(null) }

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(mapType.find()),
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
            DisposableMapEffect(earthquakes) { map ->
                Timber.e("Marker Update")

                if (clusterManager == null) {
                    clusterManager = TedNaverClustering.with<Earthquake>(context, map)
                        .markerClickListener { currentShowingMarkerInfo = it }
                        .customMarker { getCustomMaker(it) }
                        .clickToCenter(false)
                        .minClusterSize(15)
                        .make()
                }

                clusterManager?.addItems(earthquakes)
                onDispose {
                    clusterManager?.clearItems()
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
}