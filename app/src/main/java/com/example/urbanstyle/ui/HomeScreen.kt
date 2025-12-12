package com.example.urbanstyle.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.urbanstyle.R
import com.example.urbanstyle.data.model.Producto
import com.example.urbanstyle.data.repository.ProductoRepository
import com.example.urbanstyle.navigation.Rutas
import com.example.urbanstyle.ui.components.BottomBar
import com.example.urbanstyle.ui.components.ClimaCard
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val repo = remember { ProductoRepository() }
    val destacados = remember { repo.obtenerDestacados() }
    val pacifico = FontFamily(Font(R.font.pacifico))
    val clp = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pastelería Mil Sabores",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pacifico
                    )
                }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // ✅ CLIMA ARRIBA DE TODO
            item {
                ClimaCard()
            }

            // --- Header ---
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Pastelería Mil Sabores",
                    modifier = Modifier.size(180.dp),
                    contentScale = ContentScale.Fit
                )
            }

            item {
                Text(
                    text = "¡Bienvenido a nuestra pastelería, donde cada dulce cuenta una historia! 🍰",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Divider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }

            item {
                Text(
                    text = "Descubre nuestras tortas, postres y panes artesanales elaborados con amor y dedicación.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            item {
                Button(
                    onClick = {
                        navController.navigate(Rutas.PRODUCTOS) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver productos", color = MaterialTheme.colorScheme.onSecondary)
                }
            }

            // --- Título destacados ---
            item {
                Text(
                    text = "Productos destacados",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = pacifico,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --- Lista vertical de destacados ---
            items(destacados, key = { it.codigo }) { p ->
                DestacadoVerticalCard(
                    producto = p,
                    onVerDetalle = { navController.navigate("producto/${p.codigo}") },
                    onAgregarCarrito = { /* TODO: placeholder carrito */ },
                    clp = clp
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(Rutas.API_EXTERNA) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ir a Posts")
                }
            }
        }
    }
}

@Composable
private fun DestacadoVerticalCard(
    producto: Producto,
    onVerDetalle: () -> Unit,
    onAgregarCarrito: () -> Unit,
    clp: NumberFormat
) {
    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = producto.imagenRes),
            contentDescription = producto.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.padding(16.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = clp.format(producto.precio),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onVerDetalle,
                    modifier = Modifier.weight(1f)
                ) { Text("Ver detalle") }

                Button(
                    onClick = onAgregarCarrito,
                    modifier = Modifier.weight(1f)
                ) { Text("Agregar") }
            }
        }
    }
}
