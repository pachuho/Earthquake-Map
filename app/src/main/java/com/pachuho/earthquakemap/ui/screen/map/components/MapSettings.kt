package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.data.datasource.SettingResult
import com.pachuho.earthquakemap.ui.screen.map.components.settings.ButtonToggleGroup
import com.pachuho.earthquakemap.ui.screen.map.components.settings.DateRangePicker
import com.pachuho.earthquakemap.ui.screen.map.components.settings.MagnitudeRangeSlider
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingDateType
import com.pachuho.earthquakemap.ui.screen.map.components.settings.SettingMapType
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.SpacerMedium

@Composable
fun MapSettings(
    minDateTime: Long,
    startDateTime: Long,
    endDateTime: Long,
    currentMagSliderPosition: ClosedFloatingPointRange<Float>,
    currentDateType: SettingDateType,
    currentMapType: SettingMapType,
    onResult: (SettingResult) -> Unit
) {
    var isShowingDatePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {

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
                    if (it == SettingDateType.Custom) {
                        onResult(SettingResult.DateType(it))
                        isShowingDatePicker = true
                    } else {
                        onResult(SettingResult.DateType(it))
                    }
                }
            }

            SpacerLarge()

            Text(
                text = stringResource(R.string.earthquake_mag_filtering),
                style = MaterialTheme.typography.titleLarge,
            )

            SpacerMedium()

            MagnitudeRangeSlider(currentMagSliderPosition) { value ->
                onResult(SettingResult.MagnitudeRange(value.start, value.endInclusive))
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
                    onResult(SettingResult.MapType(it))
                }
            }

            SpacerLarge()
        }


        if (isShowingDatePicker) {
            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = {
                    isShowingDatePicker = false
                }
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                ) {
                    DateRangePicker(
                        minDateTime = minDateTime,
                        startDateTime = startDateTime,
                        endDateTime = endDateTime,
                        onDisMiss = {
                            isShowingDatePicker = false
                        },
                        onDateSelected = { start, end ->
                            isShowingDatePicker = false
                            onResult(SettingResult.DateStartAndEnd(start, end))
                        }
                    )
                }

            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun MapSettingsPreview() {
    val magSliderPosition by remember { mutableStateOf(1.0f..10.0f) }
    var currentMapType by remember { mutableStateOf(SettingMapType.Basic) }
    var startDateTime by remember { mutableLongStateOf(201608230145) }
    var endDateTime by remember { mutableLongStateOf(202408230145) }
    MapSettings(
        minDateTime = 201608230145,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        currentMagSliderPosition = magSliderPosition,
        currentDateType = SettingDateType.OneDay,
        currentMapType = SettingMapType.Basic
    ) { settingResult ->
        when (settingResult) {
            is SettingResult.DateType -> {}
            is SettingResult.DateStartAndEnd -> {
                startDateTime = settingResult.start
                endDateTime = settingResult.end
            }
            is SettingResult.MagnitudeRange -> {}

            is SettingResult.MapType -> {
                currentMapType = settingResult.mapType
            }
        }
    }
}