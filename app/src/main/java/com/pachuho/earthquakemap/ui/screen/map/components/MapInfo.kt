package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.ui.theme.MagGreen
import com.pachuho.earthquakemap.ui.theme.MagOrange
import com.pachuho.earthquakemap.ui.theme.MagRed
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.SpacerMedium

@Composable
fun MapInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 30.dp)
    ) {
        SpacerMedium()

        Text(
            text = stringResource(R.string.service_explain),
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        Text(
            text = stringResource(R.string.service_explain_comment),
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
        )

        SpacerLarge()

        Text(
            text = stringResource(R.string.earthquake_mag_color),
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            InfoMarker(MagGreen, "1.0 ~ 2.9")
            InfoMarker(MagOrange, "3.0 ~ 3.9")
            InfoMarker(MagRed, "4.0 ~ 10.0")
        }

        SpacerLarge()

        Text(
            text = stringResource(R.string.data_source),
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        Text(
            text = stringResource(R.string.seoul_open_api_name),
            style = MaterialTheme.typography.bodyLarge,
        )

        SpacerLarge()
    }
}

@Composable
fun InfoMarker(tint: Color, contentDescription: String) {
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
private fun MapInfoPreview() {
    MapInfo()
}

@Composable
@Preview(showBackground = true)
fun InfoMarkerPreview() {
    Row {
        InfoMarker(MagGreen, "1.0 ~ 2.9")
    }
}