package com.example.urbanstyle.data.api.clima

data class ClimaResponse(
    val name: String,          // Ciudad
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double           // Temperatura
)

data class Weather(
    val description: String
)
