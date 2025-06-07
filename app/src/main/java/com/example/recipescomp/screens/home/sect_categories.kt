package com.example.recipescomp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.data.category.Category
import com.example.recipescomp.data.category.CategoryItem
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun Categories(navController: NavController) {
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
        // More categories can be added here
    )

    Text(
        text = "Categorías",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BrownDark,
        modifier = Modifier.padding(horizontal = 16.dp)
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
}
