package com.example.recipescomp.screens


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.resourcesApi.Meal
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.components.ReusableButton
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.local.FavoriteRecipesEntity
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModelFactory
import com.example.recipescomp.ui.theme.BrownDark
import kotlinx.coroutines.launch
import com.example.recipescomp.data.local.addRecipeToShoppingListUniversal
import androidx.core.net.toUri


@Composable
fun Receta(navController: NavController, meal: Meal) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Ingredients", "Step to Step")

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())

    val viewModel: FavoriteRecipeViewModel = viewModel(factory = FavoriteRecipeViewModelFactory(repository))
    val favorites by viewModel.favorites.collectAsState()
    val isFavorite = favorites.any { it.name == meal.strMeal }
    val snackbarHostState = remember { SnackbarHostState() }

    val ingredientes = remember(meal) {
        (1..20).mapNotNull { i ->
            val ingredient = meal.javaClass.getDeclaredField("strIngredient$i").apply { isAccessible = true }.get(meal) as? String
            val measure = meal.javaClass.getDeclaredField("strMeasure$i").apply { isAccessible = true }.get(meal) as? String
            if (!ingredient.isNullOrBlank()) ingredient to (measure ?: "") else null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
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
                modifier = Modifier
                    .fillMaxSize(),
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
                    contentDescription = "Back",
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
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = meal.strMeal,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    meal.strCategory?.let {
                        Text("Category: $it", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    meal.strArea?.let {
                        Text("Country: $it", style = MaterialTheme.typography.bodySmall)
                    }

                    // 🧮 CANTIDAD DE INGREDIENTES
                    Text(
                        text = "Ingredients: ${ingredientes.size}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // ❤️ BOTÓN DE FAVORITO a la derecha
                IconButton(onClick = {
                    if (isFavorite) {
                        viewModel.deleteFavorite(meal.strMeal)
                    } else {
                        val ingredientCount = listOf(
                            meal.strIngredient1,
                            meal.strIngredient2,
                            meal.strIngredient3,
                            meal.strIngredient4,
                            meal.strIngredient5,
                            meal.strIngredient6,
                            meal.strIngredient7,
                            meal.strIngredient8,
                            meal.strIngredient9,
                            meal.strIngredient10,
                            meal.strIngredient11,
                            meal.strIngredient12,
                            meal.strIngredient13,
                            meal.strIngredient14,
                            meal.strIngredient15,
                            meal.strIngredient16,
                            meal.strIngredient17,
                            meal.strIngredient18,
                            meal.strIngredient19,
                            meal.strIngredient20
                        ).count { !it.isNullOrBlank() }

                        viewModel.insertFavorite(
                            FavoriteRecipesEntity(
                                mealId = meal.idMeal,
                                name = meal.strMeal,
                                imageUrl = meal.strMealThumb,
                                category = meal.strCategory,
                                ingredientCount = ingredientCount,
                                area = meal.strArea
                            )
                        )
                    }


                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))


            // 📑 SECCIÓN DE TABS (INGREDIENTES / PASO A PASO)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = BrownDark,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BrownDark // ← Color de la línea seleccionada
                    )
                }
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

            //Fila con el botón "Modo Cocina" y el FAB de los tres puntos
            val expanded = remember { mutableStateOf(false) }
            val isInShoppingList = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Modo Cocina
                ReusableButton(
                    "Modo Cocina",
                    onClick = {
                        navController.navigate("modoCocina/${meal.idMeal}")
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box {
                    // ⋮ Botón flotante
                    FloatingActionButton(
                        onClick = { expanded.value = true },
                        containerColor = BrownDark,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(55.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        offset = DpOffset(x = 0.dp, y = (-100).dp),
                        modifier = Modifier
                            .background(Color(0xFFFFF8DC))
                            .border(0.dp, Color.Transparent)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver video") },
                            onClick = {
                                expanded.value = false
                                meal.strYoutube?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                                    context.startActivity(intent)
                                }
                            },
                            leadingIcon = {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Agregar a lista") },
                            onClick = {
                                expanded.value = false
                                isInShoppingList.value = true

                                val ingredientesStr = ingredientes.joinToString(", ") { "${it.first} (${it.second})" }
                                scope.launch {
                                    val db = AppDatabase.getInstance(context)
                                    val dao = db.ShoppingListDao()

                                    // ✅ NORMALIZAR el mealId de forma consistente (solo trim, sin lowercase)
                                    val mealId = meal.idMeal.trim()

                                    if (mealId.isBlank()) {
                                        snackbarHostState.showSnackbar("Error: ID de receta no válido")
                                        return@launch
                                    }

                                    addRecipeToShoppingListUniversal(
                                        dao = dao,
                                        mealId = mealId,
                                        name = meal.strMeal,
                                        imageUrl = meal.strMealThumb,
                                        ingredients = ingredientesStr
                                    )
                                    snackbarHostState.showSnackbar("Added to shopping list")
                                    navController.navigate("listaCompras")
                                }
                            },
                            leadingIcon = {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            }
                        )
                    }
                }
            }
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
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

