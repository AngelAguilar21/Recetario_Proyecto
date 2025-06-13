package com.example.recipescomp.screens.home

import android.net.Uri
import com.example.recipescomp.resourcesApi.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun SearchScreen(navController: NavController, viewModel: MealViewModel) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val meals = viewModel.meals.value
    val isLoading = viewModel.isLoading.value
    val showFilterDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        // Header con título
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3E2723))
        ) {
            Text(
                text = "Buscar Recetas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Fila de búsqueda y filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Buscar recetas...", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchQuery.value.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery.value = TextFieldValue("")
                                viewModel.clearFilters()
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            // Botón de filtro
            IconButton(
                onClick = { showFilterDialog.value = true },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        Color(0xFF3E2723),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtros",
                    tint = Color.White
                )
            }

            // Botón de receta aleatoria
            IconButton(
                onClick = { viewModel.getRandomMeal() },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        Color(0xFF4CAF50),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Receta aleatoria",
                    tint = Color.White
                )
            }
        }

        // Botones de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón de búsqueda
            Button(
                onClick = {
                    if (searchQuery.value.text.isNotEmpty()) {
                        viewModel.searchMeals(searchQuery.value.text)
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3E2723),
                    contentColor = Color.White
                ),
                enabled = searchQuery.value.text.isNotEmpty() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Buscar")
                }
            }

            // Botón limpiar filtros
            OutlinedButton(
                onClick = {
                    searchQuery.value = TextFieldValue("")
                    viewModel.clearFilters()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpiar")
            }
        }

        // Mostrar el cuadro de diálogo de filtros
        if (showFilterDialog.value) {
            FilterDialog(
                viewModel = viewModel,
                onDismiss = { showFilterDialog.value = false },
                onApplyFilters = { category, area, ingredient ->
                    viewModel.applyFilters(category, area, ingredient)
                    showFilterDialog.value = false
                }
            )
        }

        // Mostrar loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF3E2723),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Cargando recetas...",
                        color = BrownDark,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Mostrar los resultados
        if (!isLoading) {
            if (meals.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Encontradas ${meals.size} recetas",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(meals) { meal ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable {
                                    navController.navigate("receta/${Uri.encode(meal.idMeal)}")
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = rememberAsyncImagePainter(meal.strMealThumb),
                                    contentDescription = meal.strMeal,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .align(Alignment.BottomCenter)
                                        .background(
                                            Color.Black.copy(alpha = 0.7f),
                                            RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = meal.strMeal,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(8.dp),
                                        textAlign = TextAlign.Center,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🍽️",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No se encontraron recetas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrownDark
                        )
                        Text(
                            text = "Intenta con otra búsqueda o filtro",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}