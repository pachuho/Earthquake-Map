package com.pachuho.earthquakemap.ui.screen.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.util.SpacerLarge
import com.pachuho.earthquakemap.ui.util.SpacerMedium
import com.pachuho.earthquakemap.ui.util.toLocalDateTimeAsString
import kotlin.math.roundToInt

@Composable
fun MarkerInfo(earthquake: Earthquake) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 30.dp)
    ) {

        SpacerMedium()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = earthquake.ORIGIN_AREA,
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        SpacerLarge()

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "규모:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = ((earthquake.MAG * 10.0).roundToInt()/ 10.0).toString(),
                color = getColorByMag(earthquake.MAG),
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "깊이:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "${earthquake.ORIGIN_DEPTH} km",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "발생 시각:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = earthquake.ORIGIN_TIME.toLocalDateTimeAsString(),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "좌표:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "${(earthquake.LAT * 100.0) / 100.0} ${(earthquake.LON  * 100.0) / 100.0}",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "지진 번호:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = earthquake.KMA_EQ_NO,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "표출 여부:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = earthquake.ISSHOW,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        SpacerLarge()

        Text(
            text = "참고 사항",
            style = MaterialTheme.typography.titleLarge,
        )

        SpacerMedium()

        Text(
            text = earthquake.NOTE1,
            style = MaterialTheme.typography.bodyLarge,
            softWrap = true,
        )

        SpacerLarge()
    }
}


@Composable
@Preview(showBackground = true)
private fun MarkerInfoPreview() {
    MarkerInfo(Earthquake(
            EQ_ID = "202400000919",
            DITC = "1",
            DITC_NM = "지진정보",
            TRANS_TIME = 202408230145,
            ORIGIN_TIME = 20240823014216,
            LAT = 36.96,
            LON = 126.31,
            MAG = 2.1,
            ORIGIN_AREA = "충남 서산시 북북서쪽 23km 해역",
            NOTE1 = "지진피해 없을 것으로 예상됨, 위 자료는 미지질조사소(USGS) 분석결과임, 위 자료는 미지질조사소(USGS) 분석결과임",
            ISSHOW = "Y",
            REGDATE = "2024-08-23 01:46:06.0",
            KMA_EQ_NO = "2024005830",
            ORIGIN_DEPTH = 9

    ))
}
