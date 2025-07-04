package com.example.recipescomp.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.local.FavoriteRecipesEntity
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import com.example.recipescomp.resourcesApi.MealViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModelFactory
import com.example.recipescomp.ui.theme.BrownDark
import java.net.URLEncoder
import java.nio.charset.StandardCharsets



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: MealViewModel) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val meals = viewModel.meals.value
    val isLoading = viewModel.isLoading.value
    val showFilterDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())

    val favviewModel: FavoriteRecipeViewModel = viewModel(factory = FavoriteRecipeViewModelFactory(repository))
    val favorites by favviewModel.favorites.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2E7))
    ) {
        // 🔺 Header con botón retroceder y barra de búsqueda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrownDark)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 25.dp,
                            start = 12.dp,
                    end = 12.dp,
                    bottom = 20.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrownDark
                )
            }


            // 📝 Barra de búsqueda
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Search Recipes...", color = BrownDark, fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp) // Ajusta aquí la altura como prefieras
                    .weight(1f)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(
                    color = BrownDark,
                    fontSize = 15.sp // Asegúrate de usar un tamaño que no se corte
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrownDark,
                    unfocusedBorderColor = BrownDark,
                    cursorColor = BrownDark,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = BrownDark,
                    unfocusedTextColor = BrownDark,
                    focusedLeadingIconColor = BrownDark,
                    unfocusedLeadingIconColor = BrownDark,
                    focusedTrailingIconColor = BrownDark,
                    unfocusedTrailingIconColor = BrownDark
                ),
                trailingIcon = {
                    if (searchQuery.value.text.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery.value = TextFieldValue("")
                            viewModel.clearFilters()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = BrownDark)
                        }
                    }
                },
                singleLine = true
            )



            // 🔍 Botón pequeño para buscar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 4.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .clickable {
                        if (searchQuery.value.text.isNotEmpty()) {
                            viewModel.searchMeals(searchQuery.value.text)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = BrownDark
                )
            }

            // ⚙️ Botón de filtro
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 4.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .clickable { showFilterDialog.value = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtros",
                    tint = BrownDark
                )
            }

        }


        // 🌿 Chips de filtros rápidos
        val selectedChip = remember { mutableStateOf("All Recipes") }

        LazyRow(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            items(listOf("All Recipes", "Random")) { label ->
                val isSelected = selectedChip.value == label
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedChip.value = label
                        if (label == "All Recipes") viewModel.fetchMeals()
                        if (label == "Random") viewModel.getRandomMeals(3)
                    },
                    label = {
                        Text(
                            label,
                            color = if (isSelected) Color.White else BrownDark,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrownDark,
                        containerColor = Color.White
                    ),
                    border = if (!isSelected) BorderStroke(1.dp, BrownDark) else null,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }


        // 📝 Texto de recetas encontradas
        if (!isLoading && meals.isNotEmpty()) {
            Text(
                text = "Founded ${meals.size} recipes...",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🍽️ Resultados de búsqueda
        if (!isLoading) {
            if (meals.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(meals) { meal ->
                        val isFavorite = favorites.any { it.name == meal.strMeal }
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable {
                                    navController.navigate("receta/${URLEncoder.encode(meal.idMeal, StandardCharsets.UTF_8.name())}")
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column {
                                Image(
                                    painter = rememberAsyncImagePainter(meal.strMealThumb),
                                    contentDescription = meal.strMeal,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(meal.strMeal, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                                        Text("${meal.strCategory} • ${meal.strArea}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    IconButton(onClick = {
                                        if (isFavorite) {
                                            favviewModel.deleteFavorite(meal.strMeal)
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

                                            favviewModel.insertFavorite(
                                                FavoriteRecipesEntity(
                                                    mealId = meal.idMeal,
                                                    name = meal.strMeal,
                                                    imageUrl = meal.strMealThumb,
                                                    category = meal.strCategory,
                                                    ingredientCount = ingredientCount
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
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🍽️", fontSize = 48.sp)
                        Text("No se encontraron recetas", fontSize = 18.sp, color = Color.DarkGray)
                        Text("Intenta con otra búsqueda o filtro", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3E2723))
            }
        }

        // 🏰 Filtro lateral como mini ventana (tu mismo FilterDialog modificado)
        if (showFilterDialog.value) {
            FilterDialog(
                viewModel = viewModel,
                onDismiss = { showFilterDialog.value = false },
                onApplyFilters = { cat, area, ing ->
                    viewModel.applyFilters(cat, area, ing)
                    showFilterDialog.value = false
                }
            )
        }
    }
}
