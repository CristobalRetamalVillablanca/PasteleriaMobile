package com.example.urbanstyle.data.repository

import com.example.urbanstyle.data.api.clima.ClimaApiClient
import com.example.urbanstyle.data.api.clima.ClimaResponse

class ClimaRepository {

    suspend fun obtenerClima(ciudad: String, apiKey: String): ClimaResponse {
        return ClimaApiClient.api.obtenerClimaPorCiudad(
            ciudad = ciudad,
            apiKey = apiKey
        )
    }
}