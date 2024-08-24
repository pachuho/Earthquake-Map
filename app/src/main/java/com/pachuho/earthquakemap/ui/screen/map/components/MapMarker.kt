package com.pachuho.earthquakemap.ui.screen.map.components

import android.animation.ValueAnimator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapMarker(
    earthquake: Earthquake,
    filterMagRange: ClosedFloatingPointRange<Float>,
    onClick: (Earthquake) -> Unit
) {
    val alpha = remember { Animatable(0f) }
    val targetAlpha = if (earthquake.MAG.toFloat() in filterMagRange) 1f else 0f

    LaunchedEffect(targetAlpha) {
        alpha.animateTo(
            targetValue = targetAlpha,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    if (alpha.value > 0f) {
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
            alpha = alpha.value,
            onClick = {
                onClick(earthquake)
                true
            }
        )
    }
}

fun getColorByMag(mag: Double): Color {
    return when (mag) {
        in 0.0..3.9 -> MagGreen
        in 4.0..5.9 -> MagOrange
        in 6.0..9.9 -> MagRed
        else -> Color.Black
    }
}