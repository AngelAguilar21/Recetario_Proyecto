package com.example.recipescomp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.recipescomp.resourcesApi.MealViewModel

@Composable
fun FilterDialog(
    viewModel: MealViewModel,
    onDismiss: () -> Unit,
    onApplyFilters: (String, String, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }
    var selectedArea by remember { mutableStateOf("") }
    var selectedIngredient by remember { mutableStateOf("") }
    var selectedFilterType by remember { mutableStateOf("category") }

    val categories = viewModel.categories.value
    val areas = viewModel.areas.value
    val ingredients = viewModel.ingredients.value

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "FILTROS DE RECETAS",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                // Tabs para seleccionar tipo de filtro
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterTab(
                        text = "Categoría",
                        isSelected = selectedFilterType == "category",
                        onClick = { selectedFilterType = "category" }
                    )
                    FilterTab(
                        text = "País",
                        isSelected = selectedFilterType == "area",
                        onClick = { selectedFilterType = "area" }
                    )
                    FilterTab(
                        text = "Ingrediente",
                        isSelected = selectedFilterType == "ingredient",
                        onClick = { selectedFilterType = "ingredient" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido del filtro seleccionado
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    when (selectedFilterType) {
                        "category" -> {
                            if (categories.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "Cargando categorías...",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(categories) { category ->
                                    FilterItem(
                                        text = category.strCategory,
                                        isSelected = selectedCategory == category.strCategory,
                                        onClick = {
                                            selectedCategory = if (selectedCategory == category.strCategory) {
                                                ""
                                            } else {
                                                category.strCategory
                                            }
                                            selectedArea = ""
                                            selectedIngredient = ""
                                        }
                                    )
                                }
                            }
                        }
                        "area" -> {
                            if (areas.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "Cargando países...",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(areas) { area ->
                                    FilterItem(
                                        text = area.strArea,
                                        isSelected = selectedArea == area.strArea,
                                        onClick = {
                                            selectedArea = if (selectedArea == area.strArea) {
                                                ""
                                            } else {
                                                area.strArea
                                            }
                                            selectedCategory = ""
                                            selectedIngredient = ""
                                        }
                                    )
                                }
                            }
                        }
                        "ingredient" -> {
                            if (ingredients.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "Cargando ingredientes...",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(ingredients) { ingredient ->
                                    FilterItem(
                                        text = ingredient.strIngredient,
                                        isSelected = selectedIngredient == ingredient.strIngredient,
                                        onClick = {
                                            selectedIngredient = if (selectedIngredient == ingredient.strIngredient) {
                                                ""
                                            } else {
                                                ingredient.strIngredient
                                            }
                                            selectedCategory = ""
                                            selectedArea = ""
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApplyFilters(selectedCategory, selectedArea, selectedIngredient)
                    onDismiss()
                },
                enabled = selectedCategory.isNotEmpty() || selectedArea.isNotEmpty() || selectedIngredient.isNotEmpty()
            ) {
                Text("Aplicar Filtro")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun FilterTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun FilterItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
    }
}