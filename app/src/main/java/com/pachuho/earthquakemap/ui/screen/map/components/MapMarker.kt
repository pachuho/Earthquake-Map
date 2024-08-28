package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.util.MarkerIcons
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingDateType
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed
import com.pachuho.earthquakemap.ui.util.toLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapMarker(
    earthquake: Earthquake,
    filterMagRange: ClosedFloatingPointRange<Float>
) {
    val alpha = remember { Animatable(0f) }
    val targetAlpha = if (earthquake.MAG.toFloat() in filterMagRange) 1f else 0f
    var currentShowingMarkerInfo by remember { mutableStateOf<Earthquake?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(targetAlpha) {
        alpha.animateTo(
            targetValue = targetAlpha,
            animationSpec = tween(durationMillis = 500)
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
            captionText = ((earthquake.MAG * 10.0).roundToInt()/ 10.0).toString(),
            alpha = alpha.value,
            onClick = {
                currentShowingMarkerInfo = earthquake
                true
            }
        )
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

fun getShowingMarker(
    currentDateType: SettingDateType,
    startDateTime: Long,
    endDateTime: Long,
    originTime: Long
): Boolean {
    val inputDateTime = originTime.toLocalDateTime()
    val currentDateTime = LocalDateTime.now()
    val daysDifference = ChronoUnit.DAYS.between(inputDateTime, currentDateTime)
    val weeksDifference = ChronoUnit.WEEKS.between(inputDateTime, currentDateTime)
    val monthsDifference = ChronoUnit.MONTHS.between(inputDateTime, currentDateTime)
    val yearsDifference = ChronoUnit.YEARS.between(inputDateTime, currentDateTime)

    return when(currentDateType) {
        SettingDateType.OneDay -> { daysDifference <= 1 }
        SettingDateType.OneWeek -> { weeksDifference <= 1}
        SettingDateType.OneMonth -> { monthsDifference <= 1}
        SettingDateType.OneYear -> { yearsDifference <= 1 }
        SettingDateType.Custom -> {
            originTime in (startDateTime..endDateTime)
        }
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