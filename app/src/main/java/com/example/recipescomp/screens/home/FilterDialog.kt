package com.example.recipescomp.screens.home

import androidx.compose.foundation.BorderStroke
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
import com.example.recipescomp.ui.theme.BrownDark

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
    val FondoTransparente = Color(0xFFF7F2E7).copy(alpha = 0.95f)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FondoTransparente,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                "FILTROS DE RECETAS",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = BrownDark
            )
        },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterTab(
                        text = "Categoría",
                        isSelected = selectedFilterType == "category",
                        onClick = { selectedFilterType = "category" },
                        brownDark = BrownDark
                    )
                    FilterTab(
                        text = "País",
                        isSelected = selectedFilterType == "area",
                        onClick = { selectedFilterType = "area" },
                        brownDark = BrownDark
                    )
                    FilterTab(
                        text = "Ingrediente",
                        isSelected = selectedFilterType == "ingredient",
                        onClick = { selectedFilterType = "ingredient" },
                        brownDark = BrownDark
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Cargando categorías...", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                items(categories) { category ->
                                    FilterItem(
                                        text = category.strCategory,
                                        isSelected = selectedCategory == category.strCategory,
                                        onClick = {
                                            selectedCategory = if (selectedCategory == category.strCategory) "" else category.strCategory
                                            selectedArea = ""
                                            selectedIngredient = ""
                                        },
                                        brownDark = BrownDark
                                    )
                                }
                            }
                        }
                        "area" -> {
                            if (areas.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Cargando países...", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                items(areas) { area ->
                                    FilterItem(
                                        text = area.strArea,
                                        isSelected = selectedArea == area.strArea,
                                        onClick = {
                                            selectedArea = if (selectedArea == area.strArea) "" else area.strArea
                                            selectedCategory = ""
                                            selectedIngredient = ""
                                        },
                                        brownDark = BrownDark
                                    )
                                }
                            }
                        }
                        "ingredient" -> {
                            if (ingredients.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Cargando ingredientes...", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                items(ingredients) { ingredient ->
                                    FilterItem(
                                        text = ingredient.strIngredient,
                                        isSelected = selectedIngredient == ingredient.strIngredient,
                                        onClick = {
                                            selectedIngredient = if (selectedIngredient == ingredient.strIngredient) "" else ingredient.strIngredient
                                            selectedCategory = ""
                                            selectedArea = ""
                                        },
                                        brownDark = BrownDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApplyFilters(selectedCategory, selectedArea, selectedIngredient)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrownDark, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedCategory.isNotEmpty() || selectedArea.isNotEmpty() || selectedIngredient.isNotEmpty()
            ) {
                Text("Aplicar Filtro")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        onApplyFilters("", "", "")
                        onDismiss()
                    },
                    border = BorderStroke(1.dp, BrownDark),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrownDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Limpiar")
                }
                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(1.dp, BrownDark),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrownDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}

@Composable
private fun FilterTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    brownDark: Color
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) brownDark else Color(0xFFE0D7CF)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else brownDark,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun FilterItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    brownDark: Color
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
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = brownDark)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = brownDark
        )
    }
}
