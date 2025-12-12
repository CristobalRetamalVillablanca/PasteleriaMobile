package com.example.urbanstyle.data.api.clima

import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApiService {

    @GET("weather")
    suspend fun obtenerClimaPorCiudad(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): ClimaResponse
}
