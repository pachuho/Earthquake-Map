package com.pachuho.earthquakemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.pachuho.earthquakemap.ui.theme.EarthquakeMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EarthquakeMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NaverMapScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen() {
    NaverMap(
        modifier = Modifier.fillMaxSize()
    ) {
        Marker(
            state = MarkerState(position = LatLng(37.532600, 127.024612)),
            captionText = "Marker in Seoul"
        )

        Marker(
            state = MarkerState(position = LatLng(37.390791, 127.096306)),
            captionText = "Marker in Pangyo"
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EarthquakeMapTheme {
        NaverMapScreen()
    }
}