package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.pachuho.earthquakemap.data.datasource.Settings
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingDateType
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed
import com.pachuho.earthquakemap.ui.util.toLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

fun filterMarkers(
    earthquakes: List<Earthquake>,
    settings: Settings
): List<Earthquake> {
    return earthquakes
        .filter { filteringByMag(settings.magStart, settings.magEnd, it.MAG) }
        .filter { filteringByTime(settings, it.ORIGIN_TIME) }
}

fun getCustomMaker(earthquake: Earthquake): Marker {
    return Marker(LatLng(earthquake.LAT, earthquake.LON)).apply {
        icon = MarkerIcons.BLACK
        iconTintColor = getColorIntByMag(earthquake.MAG)
        captionText = ((earthquake.MAG * 10.0).roundToInt()/ 10.0).toString()
        zIndex = earthquake.MAG.toInt()
    }
}

private fun filteringByMag(
    magStart: Float,
    magEnd: Float,
    mag: Double
): Boolean {
    return mag in magStart..magEnd
}

private fun filteringByTime(
    settings: Settings,
    originTime: Long
): Boolean {
    val inputDateTime = originTime.toLocalDateTime()
    val currentDateTime = LocalDateTime.now()
    val daysDifference = ChronoUnit.DAYS.between(inputDateTime, currentDateTime)
    val weeksDifference = ChronoUnit.WEEKS.between(inputDateTime, currentDateTime)
    val monthsDifference = ChronoUnit.MONTHS.between(inputDateTime, currentDateTime)
    val yearsDifference = ChronoUnit.YEARS.between(inputDateTime, currentDateTime)

    return when(settings.dateType) {
        SettingDateType.OneDay -> { daysDifference <= 1 }
        SettingDateType.OneWeek -> { weeksDifference <= 1}
        SettingDateType.OneMonth -> { monthsDifference <= 1}
        SettingDateType.OneYear -> { yearsDifference <= 1 }
        SettingDateType.Custom -> {
            originTime in (settings.dateStart..settings.dateEnd)
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

fun getColorIntByMag(mag: Double): Int {
    return when (mag) {
        in 0.0..3.9 -> MagGreen
        in 4.0..5.9 -> MagOrange
        in 6.0..9.9 -> MagRed
        else -> Color.Black
    }.run {
        this.toArgb()
    }
}