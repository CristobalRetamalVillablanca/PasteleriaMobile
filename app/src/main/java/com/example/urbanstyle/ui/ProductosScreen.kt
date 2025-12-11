package com.example.urbanstyle.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.urbanstyle.R
import com.example.urbanstyle.data.repository.ProductoRepository
import com.example.urbanstyle.navigation.Rutas
import com.example.urbanstyle.ui.cart.CartViewModel
import com.example.urbanstyle.ui.components.BottomBar
import com.example.urbanstyle.ui.components.TarjetaProducto
import com.example.urbanstyle.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch
import java.text.Normalizer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    vm: ProductoViewModel = viewModel()
) {
    val lista by vm.productos.collectAsState()

    val pacifico = FontFamily(Font(R.font.pacifico))

    val repo = remember { ProductoRepository() }
    val categorias = remember { listOf("Todas") + repo.obtenerCategorias() }

    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    var textoBusqueda by remember { mutableStateOf("") }

    fun String.normalizar(): String =
        Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
            .lowercase()

    val productosFiltrados = remember(lista, categoriaSeleccionada, textoBusqueda) {
        val q = textoBusqueda.normalizar()
        lista.filter { p ->
            val porCategoria = categoriaSeleccionada == "Todas" || p.categoria == categoriaSeleccionada
            val porNombre = q.isBlank() || p.nombre.normalizar().contains(q)
            porCategoria && porNombre
        }
    }

    // ✅ Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pacifico
                    )
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // ✅
    ) { inner ->

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 170.dp),
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp)
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar por nombre…") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (textoBusqueda.isNotBlank()) {
                            IconButton(onClick = { textoBusqueda = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                    items(categorias.size) { idx ->
                        val cat = categorias[idx]
                        val selected = cat == categoriaSeleccionada
                        FilterChip(
                            selected = selected,
                            onClick = { categoriaSeleccionada = cat },
                            label = { Text(cat) },
                            leadingIcon = { if (selected) Icon(Icons.Default.Check, null) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

            items(productosFiltrados, key = { it.codigo }) { producto ->
                TarjetaProducto(
                    producto = producto,
                    modifier = Modifier.padding(6.dp),
                    onClick = {
                        navController.navigate(
                            Rutas.PRODUCTO_DETALLE.replace("{codigo}", producto.codigo)
                        )
                    },
                    onAgregarCarrito = {
                        cartViewModel.agregarProducto(producto)

                        // ✅ Mensaje "agregado"
                        scope.launch {
                            snackbarHostState.showSnackbar("¡${producto.nombre} agregado al carrito!")
                        }
                    }
                )
            }

            if (productosFiltrados.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Sin resultados para “$textoBusqueda” en ${categoriaSeleccionada.lowercase()}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
