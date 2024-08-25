package com.pachuho.earthquakemap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Earthquake(
    @field:Json(name = "EQ_ID") @PrimaryKey val EQ_ID: String,             // 지진ID
    @field:Json(name = "DITC") val DITC: String,              // 구분
    @field:Json(name = "DITC_NM") val DITC_NM: String,         // 구분명
    @field:Json(name = "TRANS_TIME") val TRANS_TIME: Long,   // 전송시각 (예: "202408011301")
    @field:Json(name = "ORIGIN_TIME") val ORIGIN_TIME: Long, // 진원시 (예: "20240801125008")
    @field:Json(name = "LAT") val LAT: Double,                // 위도 (단위: N, 예: 38.77)
    @field:Json(name = "LON") val LON: Double,                // 경도 (단위: E, 예: 125.89)
    @field:Json(name = "MAG") val MAG: Double,                // 규모 (리히터 기준, 예: 2.6)
    @field:Json(name = "ORIGIN_AREA") val ORIGIN_AREA: String, // 진앙지 (예: "북한 황해북도 송림 동쪽 22km 지역")
    @field:Json(name = "NOTE1") val NOTE1: String,            // 참고사항
    @field:Json(name = "ISSHOW") val ISSHOW: String,          // 표출여부 (예: "Y")
    @field:Json(name = "REGDATE") val REGDATE: String,        // 등록일자
    @field:Json(name = "KMA_EQ_NO") val KMA_EQ_NO: String,      // 지진번호(기상청)
    @field:Json(name = "ORIGIN_DEPTH") val ORIGIN_DEPTH: Int    // 깊이 (단위: KM, 예: 9)
)