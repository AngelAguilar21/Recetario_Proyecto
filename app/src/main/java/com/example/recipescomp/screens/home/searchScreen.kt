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
        // Fila que contiene la barra de búsqueda y el icono de filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra de búsqueda
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Buscar recetas...", color = Color.LightGray) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Ícono de búsqueda",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)  // Esto asegura que ocupe el espacio restante en la fila
                    .background(Color.White)
                    .padding(horizontal = 10.dp),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            // Icono de filtro
            IconButton(
                onClick = { showFilterDialog.value = true },
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Ícono de filtros",
                    tint = Color.Gray
                )
            }
        }

        // Botón de búsqueda
        Button(
            onClick = { viewModel.searchMeals(searchQuery.value.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3E2723), // Cambia el color de fondo del botón
                contentColor = Color.White // Cambia el color del texto del botón
            )
        ) {
            Text("Buscar")
        }

        // Mostrar el cuadro de diálogo de filtros
        if (showFilterDialog.value) {
            FilterDialog(onDismiss = { showFilterDialog.value = false }, onApplyFilters = { category ->
                // Aplicamos el filtro
                viewModel.fetchMealsByCategory(category) // Actualiza las recetas según la categoría seleccionada
                showFilterDialog.value = false
            })
        }

        // Mostrar resultados de la búsqueda
        if (meals.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(meals) { meal ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
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
                    .padding(16.dp, 32.dp)  // Aumenté el padding superior e inferior para un mejor espaciado
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
                // Lista de categorías para elegir
                val categories = listOf("Desayuno", "Postre", "Almuerzo", "Bebida", "Saludable")
                categories.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedCategory = category // Seleccionar la categoría
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
                    onApplyFilters(selectedCategory) // Aplica el filtro con la categoría seleccionada
                    onDismiss() // Cierra el cuadro de diálogo
                }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar") // Cancela el filtro
            }
        }
    )
}
