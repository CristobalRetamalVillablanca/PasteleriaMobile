package com.example.urbanstyle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.urbanstyle.data.model.Producto
import java.text.NumberFormat
import java.util.*

@Composable
fun TarjetaProducto(
    producto: Producto,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onAgregarCarrito: (() -> Unit)? = null
) {
    val clp = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    ElevatedCard(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                )

                Text(
                    text = clp.format(producto.precio),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = { onClick?.invoke() },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        Text(
                            text = "Ver detalle",
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = { onAgregarCarrito?.invoke() },
                        enabled = onAgregarCarrito != null, // ✅ ayuda a detectar si está conectado
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        Text(
                            text = "Agregar",
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
