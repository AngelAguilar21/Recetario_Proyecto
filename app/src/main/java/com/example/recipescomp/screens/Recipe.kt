package com.example.recipescomp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.resourcesApi.Meal
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.components.ReusableButton
import com.example.recipescomp.ui.theme.BrownDark
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.local.ShoppingItemEntity
import kotlinx.coroutines.launch

@Composable
fun Receta(navController: NavController, meal: Meal) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("INGREDIENTES", "PASO A PASO")

    val isFavorite = remember { mutableStateOf(false) }
    val isInShoppingList = remember { mutableStateOf(false) }

    // ✅ Armar lista de ingredientes válidos
    val ingredientes = remember(meal) {
        (1..20).mapNotNull { i ->
            val ingredient = meal.javaClass.getDeclaredField("strIngredient$i").apply { isAccessible = true }.get(meal) as? String
            val measure = meal.javaClass.getDeclaredField("strMeasure$i").apply { isAccessible = true }.get(meal) as? String
            if (!ingredient.isNullOrBlank()) ingredient to (measure ?: "") else null
        }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        // 🔳 CABECERA CON IMAGEN Y BOTÓN
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            // 🖼 IMAGEN DE RECETA
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 🔙 BOTÓN DE REGRESO SUPERPUESTO
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 30.dp)
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
                    .clickable { navController.popBackStack() }
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = BrownDark
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .padding(horizontal = 10.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(250.dp))

            // 📝 SECCIÓN DE DESCRIPCIÓN DE LA RECETA (Nombre, categoría, región) + FAVORITO + CANTIDAD DE INGREDIENTES
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = meal.strMeal,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    meal.strCategory?.let {
                        Text("Categoría: $it", style = MaterialTheme.typography.bodySmall)
                    }

                    meal.strArea?.let {
                        Text("Región: $it", style = MaterialTheme.typography.bodySmall)
                    }

                    // 🧮 CANTIDAD DE INGREDIENTES
                    Text(
                        text = "Ingredientes: ${ingredientes.size}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // ❤️ + 🛒 BOTONES DE ACCIÓN
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ❤️ BOTÓN DE FAVORITO
                    IconButton(
                        onClick = {
                            isFavorite.value = !isFavorite.value
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite.value) Color.Red else Color.Gray
                        )
                    }

                    // 🛒 BOTÓN DE AÑADIR A LISTA DE COMPRAS (local con Room)
                    IconButton(
                        onClick = {
                            isInShoppingList.value = !isInShoppingList.value
                            if (isInShoppingList.value) {
                                val ingredientesStr = ingredientes.joinToString(", ") { "${it.first} (${it.second})" }
                                val item = ShoppingItemEntity(
                                    mealId = meal.idMeal ?: "",
                                    name = meal.strMeal,
                                    imageUrl = meal.strMealThumb ?: "",
                                    ingredients = ingredientesStr
                                )
                                scope.launch {
                                    val db = AppDatabase.getInstance(context)
                                    db.ShoppingListDao().insertItem(item)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isInShoppingList.value) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                            contentDescription = "Lista de compras",
                            tint = if (isInShoppingList.value) BrownDark else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📑 SECCIÓN DE TABS (INGREDIENTES / PASO A PASO)
            TabRow(
                selectedTabIndex = selectedTab,
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

            // 📋 CONTENIDO DE INGREDIENTES O PASOS (SEGÚN TAB SELECCIONADA)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> {
                        // 🧂 INGREDIENTES
                        items(ingredientes) { (name, measure) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color(0xFFF7F2E7), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name)
                                Text(measure, color = Color(0xFFE1B38B))
                            }
                        }
                    }

                    1 -> {
                        // 👣 PASO A PASO
                        val pasos = meal.strInstructions?.split(Regex("\r?\n"))?.filter { it.isNotBlank() } ?: listOf("No hay instrucciones disponibles.")

                        items(pasos) { paso ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(BrownDark, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${pasos.indexOf(paso) + 1}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(paso)
                            }
                        }
                    }
                }
            }

            // 🍳 BOTÓN MODO COCINA
            ReusableButton(
                "Modo Cocina",
                onClick = { navController.navigate("modoCocina") },
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        }

        // 🔽 BARRA DE NAVEGACIÓN INFERIOR
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(50))
                .background(BrownDark)
                .shadow(10.dp, RoundedCornerShape(50))
                .fillMaxWidth()
                .height(64.dp)
        )
    }
}


