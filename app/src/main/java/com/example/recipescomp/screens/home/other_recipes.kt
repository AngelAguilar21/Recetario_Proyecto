package com.example.recipescomp.screens.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.resourcesApi.Meal
@Composable
fun OtherRecipeSection(meal: Meal, navController: NavController) {
    val favorites = remember { mutableStateListOf<String>() }
    if (meal == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable {
                navController.navigate("receta/${Uri.encode(meal.idMeal)}")
            }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(meal.strMealThumb),
            contentDescription = meal.strMeal,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = meal.strMeal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Categoría: ${meal.strCategory ?: "Desconocida"}",
                fontSize = 13.sp,
                color = Color.Gray
            )

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
                meal.strIngredient10
            ).count { !it.isNullOrBlank() }

            Text(
                text = "Ingredientes: $ingredientCount",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        IconButton(
            onClick = {
                if (favorites.contains(meal.idMeal)) favorites.remove(meal.idMeal)
                else favorites.add(meal.idMeal)
            },
            modifier = Modifier.align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = if (favorites.contains(meal.idMeal)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (favorites.contains(meal.idMeal)) Color.Red else Color.Gray
            )
        }
    }


}



