package com.pachuho.earthquakemap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EarthquakeResponse(
    @Json(name = "TbEqkKenvinfo") val tbEqkKenvinfo: TbEqkKenvinfo
)

@JsonClass(generateAdapter = true)
data class TbEqkKenvinfo(
    @Json(name = "list_total_count") val listTotalCount: Int,
    @Json(name = "RESULT") val result: Result,
    @Json(name = "row") val rows: List<Earthquake>
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "CODE") val code: String,
    @Json(name = "MESSAGE") val message: String
)
