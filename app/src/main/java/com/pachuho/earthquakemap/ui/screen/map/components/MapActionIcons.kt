package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.ui.theme.Gray100

@Composable
fun MapActionIcons(
    modifier: Modifier,
    onClickIcon: (ActionIcons) -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = Color.DarkGray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .height(IntrinsicSize.Min)
    ) {

        IconDetail(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.info)
        ) {
            onClickIcon(ActionIcons.Info)
        }

        IconDivider()

        IconDetail(
            imageVector = Icons.Default.List,
            contentDescription = stringResource(R.string.earthquake_list)
        ) {
            onClickIcon(ActionIcons.List)
        }

        IconDivider()

        IconDetail(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.setting)
        ) {
            onClickIcon(ActionIcons.Settings)
        }
    }
}

@Composable
fun IconDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(Color.White.copy(alpha = 0.3f))
    )
}

@Composable
fun IconDetail(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(5.dp)
            .background(Color.Transparent, shape = CircleShape)
            .clickable(
                onClick = onClick, // 클릭 이벤트 처리
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(
            modifier = Modifier.padding(6.dp),
            imageVector = imageVector,
            tint = Gray100,
            contentDescription = contentDescription
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MapActionIconsPreview() {
    MapActionIcons(
        modifier = Modifier
            .padding(10.dp)
    ) {}
}

sealed class ActionIcons {
    data object Info : ActionIcons()
    data object List : ActionIcons()
    data object Settings : ActionIcons()
}