package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.ui.screen.map.components.settings.ButtonToggleGroup
import com.pachuho.earthquakemap.ui.screen.map.components.settings.MagnitudeRangeSlider
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingDateType
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.SpacerMedium
import com.pachuho.earthquakemap.ui.util.toLocalDateTime
import java.time.LocalDateTime

@Composable
fun MapSettings(
    firstLocalDateTime: LocalDateTime,
    currentMagSliderPosition: ClosedFloatingPointRange<Float>,
    currentDateType: SettingDateType,
    currentMapType: SettingMapType,
    onResult: (SettingsResult) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
    ) {
        SpacerMedium()

        Text(
            text = stringResource(R.string.setting_map),
            style = MaterialTheme.typography.headlineMedium,
        )

        SpacerLarge()

        Text(
            text = "기간 필터링",
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonToggleGroup(
                items = SettingDateType.getAll(),
                currentItem = currentDateType,
                textStyle = MaterialTheme.typography.labelLarge,
                padding = 6
            ) {
                onResult(SettingsResult.DateType(it))
            }
        }

        SpacerLarge()

        Text(
            text = stringResource(R.string.earthquake_mag_filtering),
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        MagnitudeRangeSlider(currentMagSliderPosition) { value ->
            onResult(SettingsResult.Magnitude(value))
        }

        SpacerLarge()

        Text(
            text = stringResource(R.string.map_type),
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonToggleGroup(
                items = SettingMapType.getAll(),
                currentItem = currentMapType
            ) {
                onResult(SettingsResult.MapType(it))
            }
        }

        SpacerLarge()
    }
}

sealed class SettingsResult {
    data class Magnitude(val value: ClosedFloatingPointRange<Float>) : SettingsResult()
    data class DateType(val value: SettingDateType) : SettingsResult()
    data class MapType(val value: SettingMapType) : SettingsResult()
}

@Composable
@Preview(showBackground = true)
fun MapSettingsPreview() {
    201608230145.toLocalDateTime().let { firstLocalDateTime ->
        val magSliderPosition by remember { mutableStateOf(1.0f..10.0f) }
        MapSettings(
            firstLocalDateTime = firstLocalDateTime,
            currentMagSliderPosition = magSliderPosition,
            currentDateType = SettingDateType.OneDay,
            currentMapType = SettingMapType.Basic
        ) {

        }
    }
}