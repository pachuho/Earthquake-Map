package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.SpacerMedium
import kotlin.math.floor

@Composable
fun MapSettings(
    magSliderPosition: ClosedFloatingPointRange<Float>,
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
            text = "지도 설정",
            style = MaterialTheme.typography.headlineMedium,
        )

        SpacerLarge()

        // 날짜 설정

        Text(
            text = "지진 규모",
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        CustomRangeSlider(magSliderPosition) { value ->
            onResult(SettingsResult.Magnitude(value))
        }

        SpacerLarge()
    }


    // 2. 지진 크기
    // 3. 지도 타입
    // 4. 마커 크기 ?
}

@Composable
fun CustomRangeSlider(
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValue: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        RangeSlider(
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

sealed class SettingsResult {
    data class Magnitude(val value: ClosedFloatingPointRange<Float>) : SettingsResult()
    data class MapType(val mapType: String) : SettingsResult()
}

@Composable
@Preview(showBackground = true)
fun MapSettingsPreview() {
    val magSliderPosition by remember { mutableStateOf(1.0f..10.0f) }
    MapSettings(magSliderPosition) {

    }
}