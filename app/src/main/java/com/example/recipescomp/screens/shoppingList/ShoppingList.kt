package com.example.recipescomp.screens.shoppingList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.local.ShoppingItemEntity
import com.example.recipescomp.ui.theme.BrownDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lista_Compras(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf<List<ShoppingItemEntity>>(emptyList()) }

    // 🚀 Cargar recetas guardadas en Room al iniciar la pantalla
    fun cargarDesdeRoom() {
        scope.launch {
            val db = AppDatabase.getInstance(context)
            items = db.ShoppingListDao().getAllItems()
        }
    }

    LaunchedEffect(true) {
        cargarDesdeRoom()
    }

    Scaffold(
        topBar = {
            // 🧭 Barra superior con botón de regreso
            TopAppBar(
                title = { Text("Lista de Compras", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrownDark)
            )
        },
        bottomBar = {
            // ⬇️ Barra inferior de navegación
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        if (items.isEmpty()) {
            // 📭 Mensaje si no hay recetas guardadas
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No has agregado recetas aún.", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recetas Seleccionadas",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                    color = BrownDark
                )

                // 📝 Lista de recetas guardadas con imagen y botón eliminar
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp).weight(1f)) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2E7))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 🍽 Imagen de la receta
                                Image(
                                    painter = rememberAsyncImagePainter(item.imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(90.dp)
                                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                // 📝 Nombre e ingredientes
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = item.name, fontSize = 18.sp, color = BrownDark)
                                    Text(text = item.ingredients, fontSize = 14.sp, color = Color.DarkGray)
                                }

                                // 🗑 Botón de eliminar receta
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val db = AppDatabase.getInstance(context)
                                            db.ShoppingListDao().deleteItem(item)
                                            cargarDesdeRoom() // recargar lista
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }

                // 🧾 Botón "Generar Lista" al fondo
                Button(
                    onClick = { navController.navigate("summary_list") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.6f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                ) {
                    Text("Generar Lista", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

