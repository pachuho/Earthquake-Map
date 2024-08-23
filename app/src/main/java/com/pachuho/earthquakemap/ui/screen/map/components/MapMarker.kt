package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.util.MarkerIcons
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapMarker(
    earthquake: Earthquake,
    onClick: (Earthquake) -> Unit
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                earthquake.LAT,
                earthquake.LON
            )
        ),
        icon = MarkerIcons.BLACK,
        iconTintColor = getColorByMag(earthquake.MAG),
        captionText = earthquake.MAG.toString(),
        onClick = {
            onClick(earthquake)
            true
        }
    )
}

fun getColorByMag(mag: Double): Color {
    return when(mag) {
        in 0.0..2.9 -> MagGreen
        in 3.0..3.9 -> MagOrange
        in 4.0..10.0 -> MagRed
        else -> Color.Black
    }
}