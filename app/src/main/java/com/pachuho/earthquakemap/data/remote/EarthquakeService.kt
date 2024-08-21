package com.pachuho.earthquakemap.data.remote

import com.pachuho.earthquakemap.data.model.EarthquakeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EarthquakeService {

    @GET("{oauth}/json/TbEqkKenvinfo/{start}/{end}/")
    suspend fun fetchEarthquakes(
        @Path("oauth") oauth: String,
        @Path("start") start: Int,
        @Path("end") end: Int,
    ): Response<EarthquakeResponse>
}