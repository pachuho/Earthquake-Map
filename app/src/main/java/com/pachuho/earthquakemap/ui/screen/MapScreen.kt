package com.pachuho.earthquakemap.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.pachuho.earthquakemap.ui.util.UiState
import com.pachuho.earthquakemap.ui.util.successOrNull

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    NaverMap(
        modifier = Modifier.fillMaxSize()
    ) {
//        Marker(
//            state = MarkerState(position = LatLng(37.532600, 127.024612)),
//            captionText = "Marker in Seoul"
//        )
//
//        Marker(
//            state = MarkerState(position = LatLng(37.390791, 127.096306)),
//            captionText = "Marker in Pangyo"
//        )

        LaunchedEffect(Unit) {
        }

        when (uiState.value) {
            is UiState.Loading -> {
                Log.e("asdf", "MapScreen: Loading")
            }

            is UiState.Error -> {
                Log.e("asdf", "MapScreen, Error: ${uiState.value}")
            }

            is UiState.Success -> {
                Log.e("asdf", "MapScreen, Success: ${uiState.value}")
                uiState.value.successOrNull()?.let { earthquakes ->
                    earthquakes
                        .filter { it.mag >= 3 }
                        .forEach { earthquake ->
                        Marker(
                            state = MarkerState(position = LatLng(earthquake.lat, earthquake.lon)),
                            captionText = earthquake.mag.toString()
                        )
                    }
                }
            }
        }


    }
}