package com.pachuho.earthquakemap.ui.screen.map.components.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <T> ButtonToggleGroup(
    items: List<T>,
    currentItem: T,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    padding: Int = 10,
    onClick: (mapType: T) -> Unit = {}
) {
    val cornerRadius = 8.dp

    Row {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 ->
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(if (items.indexOf(currentItem) == index) 1f else 0f)

                    else ->
                        Modifier
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (items.indexOf(currentItem) == index) 1f else 0f)
                },
                contentPadding = PaddingValues(padding.dp),
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
                    1.dp, if (items.indexOf(currentItem) == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.75f)
                    }
                ),
                colors = if (items.indexOf(currentItem) == index) {
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
                    text = when(currentItem) {
                        is SettingMapType -> (item as SettingMapType).value
                        is SettingDateType -> (item as SettingDateType).value
                        else -> item.toString()
                    },
                    style = textStyle,
                    color = if (items.indexOf(currentItem) == index) {
                        Color.White
                    } else {
                        Color.DarkGray.copy(alpha = 0.9f)
                    },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DateButtonToggleGroupPreview() {
    var currentDateType by remember { mutableStateOf(SettingDateType.OneYear) }
    ButtonToggleGroup(
        items = SettingDateType.getAll(),
        currentItem = currentDateType,
        textStyle = MaterialTheme.typography.labelLarge,
        padding = 6
    ) {
        currentDateType = it
    }
}

@Composable
@Preview(showBackground = true)
fun MapButtonToggleGroupPreview() {
    var currentItem by remember { mutableStateOf(SettingMapType.Basic) }
    ButtonToggleGroup(
        items = SettingMapType.getAll(),
        currentItem = currentItem
    ) {
        currentItem = it
    }
}