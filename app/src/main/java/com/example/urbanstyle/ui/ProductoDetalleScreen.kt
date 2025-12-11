package com.example.urbanstyle.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.urbanstyle.R
import com.example.urbanstyle.data.model.Producto
import com.example.urbanstyle.ui.components.BottomBar
import com.example.urbanstyle.ui.cart.CartViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreen(
    navController: NavHostController,
    producto: Producto?,
    codigo: String,
    cartViewModel: CartViewModel          // ✅ SIN default
) {
    val pacifico = FontFamily(Font(R.font.pacifico))
    val clp = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle de producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = pacifico
                    )
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { inner ->

        if (producto == null) {
            Box(
                modifier = Modifier.padding(inner).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Producto no encontrado ($codigo)") }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Text(producto.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            AssistChip(onClick = { }, label = { Text(producto.categoria) })

            Text(
                clp.format(producto.precio),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(producto.descripcion, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        cartViewModel.agregarProducto(producto)
                        scope.launch {
                            snackbarHostState.showSnackbar("¡${producto.nombre} agregado al carrito!")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Agregar al carrito") }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) { Text("Volver") }
            }
        }
    }
}
