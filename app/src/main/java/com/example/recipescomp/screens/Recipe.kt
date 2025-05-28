package com.example.recipescomp.screens

import BackButton
import BottomNavigationBar
import ReusableButton
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavController
import com.example.recipescomp.R
import com.example.recipescomp.shared.Ingrediente
import com.example.recipescomp.shared.ListaDeComprasState
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun Receta(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("INGREDIENTES", "PASO A PASO")
    var portions by remember { mutableStateOf(1) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            // IMAGEN DE LA RECETA
            Image(
                painter = painterResource(id = R.drawable.receta_image), // reemplaza con tu imagen
                contentDescription = "Imagen de la receta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ENCABEZADO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BackButton(onClick = { navController.popBackStack() })

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "NOMBRE DE LA RECETA",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Descripción...",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    containerColor = Color.Transparent,
                    contentColor = BrownDark
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) BrownDark else Color.Gray
                                )
                            }
                        )
                    }
                }
            }

            // CONTENIDO PRINCIPAL
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> {
                        val ingredientes = listOf(
                            "Ingrediente 1" to "Cantidad y Medida",
                            "Ingrediente 2" to "Cantidad y Medida",
                            "Ingrediente 3" to "Cantidad y Medida",
                            "Ingrediente 4" to "Cantidad y Medida"
                        )

                        LazyColumn(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                        .background(Color(0xFFF7F2E7), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    IconButton(
                                        onClick = { if (portions > 1) portions-- },
                                        enabled = portions > 1,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                color = if (portions > 1) BrownDark else Color.Gray,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.minus_icon),
                                            contentDescription = "Disminuir porciones",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = "$portions PORCIONES",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFF6B3E15)
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    IconButton(
                                        onClick = { portions++ },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                color = BrownDark,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.plus_icon),
                                            contentDescription = "Aumentar porciones",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }

                            items(ingredientes) { (name, measure) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Color(0xFFF7F2E7), RoundedCornerShape(8.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = name,
                                        fontWeight = FontWeight.Medium,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF6B3E15)
                                    )
                                    Text(
                                        text = measure,
                                        color = Color(0xFFE1B38B),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        val pasos = listOf("Paso 1", "Paso 2", "Paso 3", "Paso 4")

                        LazyColumn(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            itemsIndexed(pasos) { index, step ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(BrownDark, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "${index + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(step)
                                }
                            }
                        }
                    }
                }
            }
        }

        // MENÚ EMERGENTE Y BOTÓN FLOTANTE
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (isMenuExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            isMenuExpanded = false
                        }
                )
            }

            FloatingActionButton(
                onClick = { isMenuExpanded = !isMenuExpanded },
                containerColor = Color.White, // Fondo blanco
                elevation = FloatingActionButtonDefaults.elevation(8.dp), // Sombra
                modifier = Modifier.padding(bottom = 72.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Abrir menú",
                    tint = BrownDark // Ícono marrón
                )
            }

            if (isMenuExpanded) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(bottom = 56.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = { /* Acción favoritos */ },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favoritos",
                            tint = BrownDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IconButton(
                        onClick = { /* Acción descargar */ },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.descar),
                            contentDescription = "Descargar",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IconButton(
                        onClick = {
                            val receta = com.example.recipescomp.shared.Receta(
                                id = 1,
                                nombre = "NOMBRE DE LA RECETA",
                                imagen = "ruta_o_url",
                                ingredientes = listOf( // ← ESTA PARTE FALTABA
                                    Ingrediente("Harina", "1 taza"),
                                    Ingrediente("Huevo", "2 unidades"),
                                    Ingrediente("Azúcar", "3 cucharadas")
                                )
                            )


                            val fueAgregada = ListaDeComprasState.agregarReceta(receta)

                            Toast.makeText(
                                context,
                                if (fueAgregada) "Receta agregada a la lista" else "La receta ya está en la lista",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_cart),
                            contentDescription = "Carrito",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // BARRA INFERIOR
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(BrownDark)
                .padding(vertical = 16.dp)
        )
    }
}