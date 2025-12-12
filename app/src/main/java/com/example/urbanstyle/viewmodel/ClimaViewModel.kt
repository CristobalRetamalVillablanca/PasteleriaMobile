package com.example.urbanstyle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanstyle.data.repository.ClimaRepository
import com.example.urbanstyle.ui.clima.ClimaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClimaViewModel : ViewModel() {

    private val repository = ClimaRepository()

    private val _uiState = MutableStateFlow(ClimaUiState(cargando = true))
    val uiState: StateFlow<ClimaUiState> = _uiState.asStateFlow()

    private val API_KEY = "516ef28178ecd7adaec912a3c14e2021"

    init {
        cargarClima("Santiago")
    }

    fun cargarClima(ciudad: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ClimaUiState(cargando = true)

                val clima = repository.obtenerClima(ciudad, API_KEY)

                _uiState.value = ClimaUiState(
                    ciudad = clima.name,
                    temperatura = "${clima.main.temp.toInt()}°C",
                    descripcion = clima.weather.firstOrNull()?.description ?: "",
                    cargando = false
                )

            } catch (e: Exception) {
                _uiState.value = ClimaUiState(
                    error = "Error clima: ${e.message}",
                    cargando = false
                )
            }
        }
    }
}
