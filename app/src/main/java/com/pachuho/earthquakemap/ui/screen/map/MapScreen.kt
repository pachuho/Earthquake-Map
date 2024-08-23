package com.pachuho.earthquakemap.ui.screen.map

import android.accounts.NetworkErrorException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.util.MarkerIcons
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.screen.map.components.BaseIcon
import com.pachuho.earthquakemap.ui.screen.map.components.MapInfo
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.successOrNull
import timber.log.Timber
import kotlin.math.floor

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
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val seoul = LatLng(37.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(seoul, 7.0)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
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
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(
                                            earthquake.LAT,
                                            earthquake.LON
                                        )
                                    ),
                                    icon = MarkerIcons.BLACK,
                                    iconTintColor = getMakerColor(earthquake.MAG),
                                    captionText = earthquake.MAG.toString()
                                )
                            }
                    }
                }
                is UiState.Error -> {}
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
        ) {
            BaseIcon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.my_location)
            ) {}

            BaseIcon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.explain)
            ) {
                isShowingInfo = !isShowingInfo
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
}

private fun getMakerColor(mag: Double): Color {
    return when(mag) {
        in 0.0..2.9 -> MagGreen
        in 3.0..3.9 -> MagOrange
        in 4.0..10.0 -> MagRed
        else -> Color.Black
    }
}

@Composable
fun RangeSlider(title: String) {
    var sliderPosition by remember { mutableStateOf(0f..100f) }

    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        RangeSlider(
            value = sliderPosition.start..sliderPosition.endInclusive,
            onValueChange = { range ->
                val startValue = range.start
                val endValue = range.endInclusive

                if (startValue != endValue) {
                    sliderPosition = startValue..endValue
                }
            },
            valueRange = 0f..100f,
            steps = 9,
            onValueChangeFinished = {
                // todo add data changed
            }
        )

        Text(
            text = "${floor(sliderPosition.start).div(10)} ~ ${
                floor(sliderPosition.endInclusive).div(
                    10
                )
            }"
        )
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