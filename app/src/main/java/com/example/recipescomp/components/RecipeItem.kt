package com.example.recipescomp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.recipescomp.R

@Composable
fun RecipeItem(recipe: Recipe, navController: NavController) {
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { /* Navegar al detalle de la receta */ }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Imagen de la receta
            Image(
                painter = painterResource(id = R.drawable.receta_image), // Reemplaza con tu imagen
                contentDescription = "Imagen de la receta",
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Información de la receta
            Column(modifier = Modifier.weight(1f)) {
                // Título de la receta
                Text(
                    text = recipe.title,
                    fontSize = 18.sp,
                    color = Color(0xFF6B3E15),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tiempo y número de ingredientes con íconos
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ícono del reloj
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock), // Ícono de reloj
                        contentDescription = "Tiempo",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = recipe.time,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // Ícono de ingredientes
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ingredients), // Ícono de ingredientes
                        contentDescription = "Ingredientes",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = recipe.ingredients, // Número de ingredientes
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Nombre del creador
                Text(
                    text = "Por ${recipe.creator}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Ícono de favorito
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (isFavorite) Color.Red else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { isFavorite = !isFavorite }
            )
        }
    }
}
data class Recipe(
    val title: String,
    val creator: String,
    val time: String,
    val ingredients: String
)
