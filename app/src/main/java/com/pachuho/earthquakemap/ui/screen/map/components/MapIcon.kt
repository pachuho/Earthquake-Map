package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BaseIcon(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(5.dp)
            .background(Color.White, shape = CircleShape)
            .clickable(
                onClick = onClick, // 클릭 이벤트 처리
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(
            modifier = Modifier.padding(6.dp),
            imageVector = imageVector,
            tint = Color.Black,
            contentDescription = contentDescription
        )
    }
}