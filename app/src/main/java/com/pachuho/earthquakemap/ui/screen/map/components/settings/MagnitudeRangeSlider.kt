package com.pachuho.earthquakemap.ui.screen.map.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MagnitudeRangeSlider(
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValue: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
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
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (1..10).map { "${it}.0" }.forEach {
                Text(text = it)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MagnitudeRangeSliderPreview() {
    MagnitudeRangeSlider(1.0f..10.0f) {}
}