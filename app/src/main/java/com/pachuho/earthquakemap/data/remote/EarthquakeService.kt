package com.pachuho.earthquakemap.data.remote

import com.pachuho.earthquakemap.data.model.EarthquakeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EarthquakeService {

    @GET("{oauth}/json/TbEqkKenvinfo/1/1000/")
    suspend fun fetchEarthquakes(
        @Path("oauth") oauth: String
    ): Response<EarthquakeResponse>
}