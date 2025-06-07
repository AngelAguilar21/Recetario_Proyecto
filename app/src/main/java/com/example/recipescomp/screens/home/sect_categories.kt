package com.example.recipescomp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.data.category.Category
import com.example.recipescomp.data.category.CategoryItem
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun CategoriesSection(navController: NavController) {
    // 📂 SECCIÓN 2: Categorías

    Spacer(modifier = Modifier.height(24.dp))

    // 🏷️ Título de categorías
    Text(
        text = "Categorías",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BrownDark,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))



    // 🔢 Lista horizontal de categorías
    val categories = listOf(
        Category(
            "1", "Desayunos",
            iconComposable = {
                Icon(
                    imageVector = Icons.Default.FreeBreakfast,
                    contentDescription = "Desayuno",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("desayuno")
                        }
                )
            }
        ),
        Category(
            "2", "Postres",
            iconComposable = {
                Icon(
                    imageVector = Icons.Default.Cake,
                    contentDescription = "Postres",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("postre")
                        }
                )
            }
        ),
        Category(
            "3", "Almuerzos",
            iconComposable = {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = "Almuerzo",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("almuerzo")
                        }
                )
            }
        ),
        Category(
            "4", "Bebidas",
            iconComposable = {
                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = "Bebidas",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("bebida")
                        }
                )
            }
        ),
        Category(
            "5", "Saludables",
            iconComposable = {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = "Saludable",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("saludable")
                        }
                )
            }
        )
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            CategoryItem(
                icon = category.iconComposable,
                categoryName = category.name
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}



