package com.example.urbanstyle.ui.components

import androidx.compose.foundation.layout.padding   // ✅ IMPORTANTE
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.urbanstyle.viewmodel.ClimaViewModel

@Composable
fun ClimaCard(
    vm: ClimaViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Card(
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        when {
            state.cargando -> {
                Text(
                    text = "Cargando clima...",
                    modifier = Modifier.padding(16.dp)
                )
            }

            state.error != null -> {
                Text(
                    text = state.error!!,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                Text(
                    text = "Hoy en ${state.ciudad}: ${state.temperatura} – Ideal para algo dulce ☕🍰",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
