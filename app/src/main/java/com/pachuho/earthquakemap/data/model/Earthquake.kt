package com.pachuho.earthquakemap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Earthquake(
    @Json(name = "EQ_ID") val eqId: String,             // 지진ID
    @Json(name = "DITC") val ditc: String,              // 구분
    @Json(name = "DITC_NM") val ditcNm: String,         // 구분명
    @Json(name = "TRANS_TIME") val transTime: String,   // 전송시각 (예: "202408011301")
    @Json(name = "ORIGIN_TIME") val originTime: String, // 진원시 (예: "20240801125008")
    @Json(name = "LAT") val lat: Double,                // 위도 (단위: N, 예: 38.77)
    @Json(name = "LON") val lon: Double,                // 경도 (단위: E, 예: 125.89)
    @Json(name = "MAG") val mag: Double,                // 규모 (리히터 기준, 예: 2.6)
    @Json(name = "ORIGIN_AREA") val originArea: String, // 진앙지 (예: "북한 황해북도 송림 동쪽 22km 지역")
    @Json(name = "NOTE1") val note: String,            // 참고사항
    @Json(name = "ISSHOW") val isShow: String,          // 표출여부 (예: "Y")
    @Json(name = "REGDATE") val regDate: String,        // 등록일자
    @Json(name = "KMA_EQ_NO") val kmaEqNo: String,      // 지진번호(기상청)
    @Json(name = "ORIGIN_DEPTH") val originDepth: Int    // 깊이 (단위: KM, 예: 9)
)