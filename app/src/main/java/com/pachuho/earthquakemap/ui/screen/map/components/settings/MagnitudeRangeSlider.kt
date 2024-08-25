package com.pachuho.earthquakemap.ui.screen.map.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
fun MagnitudeRangeSlider(
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
        )

        Text(
            text = "${floor(sliderPosition.start)} ~ " +
                    "${floor(sliderPosition.endInclusive)}"
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MagnitudeRangeSliderPreview() {
    MagnitudeRangeSlider(1.0f..10.0f) {}
}