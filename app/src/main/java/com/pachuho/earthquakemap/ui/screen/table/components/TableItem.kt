package com.pachuho.earthquakemap.ui.screen.table.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.pachuho.earthquakemap.data.model.Earthquake
import com.pachuho.earthquakemap.ui.theme.Orange200

@Composable
fun TableItem(earthquake: Earthquake) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {

        Column {
            Text(
                text = earthquake.ORIGIN_AREA,
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = earthquake.ORIGIN_TIME.toString(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }












































}

@Composable
@Preview(showBackground = true)
fun TableItemPreview() {
    TableItem(Earthquake(
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