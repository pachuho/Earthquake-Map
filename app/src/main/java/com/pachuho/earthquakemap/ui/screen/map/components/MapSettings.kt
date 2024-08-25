package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.naver.maps.map.compose.MapType
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.SpacerMedium
import kotlin.math.floor

@Composable
fun MapSettings(
    currentMagSliderPosition: ClosedFloatingPointRange<Float>,
    currentMapType: SettingMapType,
    onResult: (SettingsResult) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 30.dp)
    ) {
        SpacerMedium()

        Text(
            text = stringResource(R.string.setting_map),
            style = MaterialTheme.typography.headlineMedium,
        )

        SpacerLarge()

        // 날짜 설정

        Text(
            text = stringResource(R.string.earthquake_mag),
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        CustomRangeSlider(currentMagSliderPosition) { value ->
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
            MaterialButtonToggleGroup(currentMapType) {
                onResult(SettingsResult.MapType(it))
            }
        }

        SpacerLarge()
    }
}

@Composable
private fun CustomRangeSlider(
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValue: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        RangeSlider(
            modifier = Modifier.padding(horizontal = 6.dp),
            value = sliderPosition.start..sliderPosition.endInclusive,
            onValueChange = { range ->
                val startValue = range.start
                val endValue = range.endInclusive

                if (startValue != endValue) {
                    onValue(startValue..endValue)
                }
            },
            valueRange = 1f..10f,
            steps = 8,
            onValueChangeFinished = {

            }
        )

        Text(
            text = "${floor(sliderPosition.start)} ~ " +
                    "${floor(sliderPosition.endInclusive)}"
        )
    }
}

@Composable
fun MaterialButtonToggleGroup(
    currentMapType: SettingMapType,
    onClick: (mapType: SettingMapType) -> Unit = {}
) {
    val items = SettingMapType.getMapType()
    val cornerRadius = 8.dp

    Row {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 ->
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(if (currentMapType.ordinal == index) 1f else 0f)

                    else ->
                        Modifier
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (currentMapType.ordinal == index) 1f else 0f)
                },
                onClick = {
                    onClick(item)
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )
                    items.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )
                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                },
                border = BorderStroke(
                    1.dp, if (currentMapType.ordinal == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.75f)
                    }
                ),
                colors = if (currentMapType.ordinal == index) {
                    // selected colors
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    // not selected colors
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                Text(
                    text = item.value,
                    color = if (currentMapType.ordinal == index) {
                        Color.White
                    } else {
                        Color.DarkGray.copy(alpha = 0.9f)
                    },
                )
            }
        }
    }
}

sealed class SettingsResult {
    data class Magnitude(val value: ClosedFloatingPointRange<Float>) : SettingsResult()
    data class MapType(val mapType: SettingMapType) : SettingsResult()
}

enum class SettingMapType(val value: String) {
    Basic("기본"),
    Navi("네비게이션"),
    Hybrid("위성"),
    Terrain("지형도");

    companion object {
        fun getMapType(): List<SettingMapType> {
            return entries.map { it }
        }

        fun SettingMapType.find(): MapType {
            return entries
                .find { this == it }
                ?.let {
                    when (it) {
                        Basic -> MapType.Basic
                        Navi -> MapType.Navi
                        Hybrid -> MapType.Hybrid
                        Terrain -> MapType.Terrain
                    }
                } ?: run {
                MapType.Basic
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CustomRangeSliderPreview() {
    CustomRangeSlider(1.0f..10.0f) {}
}

@Composable
@Preview(showBackground = true)
fun MaterialButtonToggleGroupPreview() {
    MaterialButtonToggleGroup(SettingMapType.Basic) {}
}

@Composable
@Preview(showBackground = true)
fun MapSettingsPreview() {
    val magSliderPosition by remember { mutableStateOf(1.0f..10.0f) }
    MapSettings(magSliderPosition, SettingMapType.Basic) {

    }
}