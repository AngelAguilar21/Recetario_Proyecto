package com.example.recipescomp.screens.home

import android.net.Uri
import com.example.recipescomp.resourcesApi.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    val showFilterDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        // Campo de texto para ingresar la búsqueda
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            placeholder = { Text("Buscar recetas...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Ícono de búsqueda",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        // Icono de filtro
        IconButton(
            onClick = {
                showFilterDialog.value = true  // Muestra el cuadro de diálogo de filtros
            },
            modifier = Modifier
                .size(56.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Ícono de filtros",
                tint = Color.Gray
            )
        }

        // Mostrar filtros en un cuadro de diálogo
        if (showFilterDialog.value) {
            FilterDialog(onDismiss = { showFilterDialog.value = false }, onApplyFilters = { category ->
                // Aplica el filtro (en este caso, por categoría)
                viewModel.fetchMealsByCategory(category)
                showFilterDialog.value = false
            })
        }

        // Botón de búsqueda
        Button(
            onClick = { viewModel.searchMeals(searchQuery.value.text) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Buscar")
        }

        // Mostrar resultados de la búsqueda
        if (meals.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(meals) { meal ->
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .shadow(6.dp, shape = RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate("receta/${Uri.encode(meal.idMeal)}")
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(meal.strMealThumb),
                            contentDescription = meal.strMeal,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .align(Alignment.BottomCenter)
                                .background(Color.White.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = meal.strMeal,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            // Si no hay resultados, mostrar mensaje
            Text(
                text = "No se encontraron recetas",
                fontSize = 18.sp,
                color = BrownDark,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun FilterDialog(onDismiss: () -> Unit, onApplyFilters: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("Desayuno") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar por categoría") },
        text = {
            Column {
                val categories = listOf("Desayuno", "Postre", "Almuerzo", "Bebida", "Saludable")
                categories.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedCategory = category
                            }
                    ) {
                        RadioButton(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = category)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApplyFilters(selectedCategory)
                    onDismiss()
                }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
