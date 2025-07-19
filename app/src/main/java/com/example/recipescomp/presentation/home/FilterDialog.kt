package com.example.recipescomp.presentation.home

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
import com.example.recipescomp.viewmodel.MealViewModel
import com.example.recipescomp.core.theme.BrownDark

@Composable
fun FilterDialog(
    viewModel: MealViewModel,
    onDismiss: () -> Unit,
    onApplyFilters: (List<String>, List<String>, List<String>) -> Unit
) {
    var selectedCategories by remember { mutableStateOf(listOf<String>()) }
    var selectedAreas by remember { mutableStateOf(listOf<String>()) }
    var selectedIngredients by remember { mutableStateOf(listOf<String>()) }
    var selectedFilterType by remember { mutableStateOf("category") }

    val categories = viewModel.categories.value
    val areas = viewModel.areas.value
    val ingredients = viewModel.ingredients.value
    val fondoTransparente = Color(0xFFF7F2E7).copy(alpha = 0.95f)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = fondoTransparente,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                "Recipe Filters",
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
                        text = "Category",
                        isSelected = selectedFilterType == "category",
                        onClick = { selectedFilterType = "category" },
                        brownDark = BrownDark,
                        selectedCount = selectedCategories.size
                    )
                    FilterTab(
                        text = "Country",
                        isSelected = selectedFilterType == "area",
                        onClick = { selectedFilterType = "area" },
                        brownDark = BrownDark,
                        selectedCount = selectedAreas.size
                    )
                    FilterTab(
                        text = "Ingredients",
                        isSelected = selectedFilterType == "ingredient",
                        onClick = { selectedFilterType = "ingredient" },
                        brownDark = BrownDark,
                        selectedCount = selectedIngredients.size
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar contador de seleccionados
                when (selectedFilterType) {
                    "category" -> {
                        if (selectedCategories.isNotEmpty()) {
                            Text(
                                "Selected: ${selectedCategories.size} categories",
                                fontSize = 12.sp,
                                color = BrownDark,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    "area" -> {
                        if (selectedAreas.isNotEmpty()) {
                            Text(
                                "Selected: ${selectedAreas.size} countries",
                                fontSize = 12.sp,
                                color = BrownDark,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    "ingredient" -> {
                        if (selectedIngredients.isNotEmpty()) {
                            Text(
                                "Selected: ${selectedIngredients.size} ingredients",
                                fontSize = 12.sp,
                                color = BrownDark,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

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
                                            Text("Loading categories...", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                items(categories) { category ->
                                    FilterItem(
                                        text = category.strCategory,
                                        isSelected = selectedCategories.contains(category.strCategory),
                                        onClick = {
                                            selectedCategories = if (selectedCategories.contains(category.strCategory)) {
                                                selectedCategories - category.strCategory
                                            } else {
                                                selectedCategories + category.strCategory
                                            }
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
                                            Text("Loading countries...", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                items(areas) { area ->
                                    FilterItem(
                                        text = area.strArea,
                                        isSelected = selectedAreas.contains(area.strArea),
                                        onClick = {
                                            selectedAreas = if (selectedAreas.contains(area.strArea)) {
                                                selectedAreas - area.strArea
                                            } else {
                                                selectedAreas + area.strArea
                                            }
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
                                            Text("Loading ingredients...", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                items(ingredients) { ingredient ->
                                    FilterItem(
                                        text = ingredient.strIngredient,
                                        isSelected = selectedIngredients.contains(ingredient.strIngredient),
                                        onClick = {
                                            selectedIngredients = if (selectedIngredients.contains(ingredient.strIngredient)) {
                                                selectedIngredients - ingredient.strIngredient
                                            } else {
                                                selectedIngredients + ingredient.strIngredient
                                            }
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
                    onApplyFilters(selectedCategories, selectedAreas, selectedIngredients)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrownDark, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedCategories.isNotEmpty() || selectedAreas.isNotEmpty() || selectedIngredients.isNotEmpty()
            ) {
                Text("Apply Filter")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        selectedCategories = emptyList()
                        selectedAreas = emptyList()
                        selectedIngredients = emptyList()
                        onApplyFilters(emptyList(), emptyList(), emptyList())
                        onDismiss()
                    },
                    border = BorderStroke(1.dp, BrownDark),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrownDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Clean")
                }
                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(1.dp, BrownDark),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrownDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
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
    brownDark: Color,
    selectedCount: Int = 0
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.White else brownDark,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
            )
            if (selectedCount > 0) {
                Text(
                    text = "($selectedCount)",
                    color = if (isSelected) Color.White else brownDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
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
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = brownDark,
                uncheckedColor = brownDark.copy(alpha = 0.6f),
                checkmarkColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = brownDark,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}