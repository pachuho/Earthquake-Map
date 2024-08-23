package com.pachuho.earthquakemap.ui.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed

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

@Composable
fun LegendForMarker(tint: Color, contentDescription: String) {
    Column {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(60.dp)
                .padding(6.dp),
            imageVector = Icons.Filled.LocationOn,
            tint = tint,
            contentDescription = contentDescription
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = contentDescription,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LegendForMarkerPreview() {
    Row {
        LegendForMarker(MagGreen, "1.0 ~ 2.9")
    }
}