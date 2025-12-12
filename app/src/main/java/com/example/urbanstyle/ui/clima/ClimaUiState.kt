package com.example.urbanstyle.ui.clima

data class ClimaUiState(
    val ciudad: String = "",
    val temperatura: String = "",
    val descripcion: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)
