package com.example.recipescomp.screens.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.data.category.Category
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun CategoriesSection(navController: NavController) {

    // Título de categorías
    Text(
        text = "Categorías",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BrownDark,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))

    val categories = listOf(
        Category("1", "Breakfast", Icons.Default.FreeBreakfast, "Breakfast"),
        Category("2", "Dessert", Icons.Default.Cake, "Dessert"),
        Category("3", "Pasta", Icons.Default.RestaurantMenu, "Pasta"),
        Category("4", "Pizza", Icons.Default.LocalPizza, "Pizza"),
        Category("5", "Vegetarian", Icons.Default.Spa, "Vegetarian")
    )

    LazyRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            CategoryItemView(category, navController)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun CategoryItemView(category: Category, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                navController.navigate("category/${category.route}")
            }
            .padding(bottom = 8.dp)
    ) {
        // Ícono de la categoría
        Icon(
            imageVector = category.iconComposable,
            contentDescription = category.name,
            modifier = Modifier
                .size(50.dp)
        )

        // Nombre de la categoría
        Text(
            text = category.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BrownDark,
            modifier = Modifier.padding(top = 4.dp)  // Espacio entre el ícono y el nombre
        )
    }
}
