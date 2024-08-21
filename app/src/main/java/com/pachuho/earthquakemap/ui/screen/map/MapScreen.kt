package com.pachuho.earthquakemap.ui.screen.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.showToast
import com.pachuho.earthquakemap.ui.util.successOrNull
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isShowingSettingContents by remember { mutableStateOf(false) }

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
            when (uiState.value) {
                is UiState.Loading -> {
                    Log.e("asdf", "MapScreen: Loading")
                }

                is UiState.Success -> {
                    uiState.value.successOrNull()?.let { earthquakes ->
                        earthquakes
                            .forEach { earthquake ->
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(
                                            earthquake.LAT,
                                            earthquake.LON
                                        )
                                    ),
                                    captionText = earthquake.MAG.toString()
                                )
                            }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
        ) {
            BaseIcon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "내 위치"
            ) {
                context.showToast("업데이트 예정")
                // todo
            }

            BaseIcon(
                imageVector = Icons.Default.Settings,
                contentDescription = "설정"
            ) {
                isShowingSettingContents = !isShowingSettingContents
            }
        }
    }

    if (isShowingSettingContents) {
        ModalBottomSheet(
            onDismissRequest = {
                isShowingSettingContents = false
            }
        ) {
            MapContents()
        }
    }
}

@Composable
private fun BaseIcon(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(5.dp)
            .background(Color.White, shape = CircleShape)
            .clickable(
                onClick = onClick, // 클릭 이벤트 처리
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(
            modifier = Modifier.padding(6.dp),
            imageVector = imageVector,
            tint = Color.Black,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun MapContents() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        RangeSlider("지진 규모 (리히터)")
        Spacer(modifier = Modifier.height(64.dp))
    }
}



@Composable
fun RangeSlider(title: String) {
    var sliderPosition by remember { mutableStateOf(0f..100f) }

    Column{
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

        Text(text = "${floor(sliderPosition.start).div(10)} ~ ${floor(sliderPosition.endInclusive).div(10)}")
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 480)
fun MapContentsPreview() {
    MapContents()
}