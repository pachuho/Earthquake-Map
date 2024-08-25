package com.pachuho.earthquakemap.ui.screen.map.components.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.naver.maps.map.compose.MapType

@Composable
fun ButtonToggleGroup(
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
fun MaterialButtonToggleGroupPreview() {
    ButtonToggleGroup(SettingMapType.Basic) {}
}