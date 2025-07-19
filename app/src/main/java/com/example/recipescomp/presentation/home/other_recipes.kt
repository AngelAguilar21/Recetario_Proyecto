package com.example.recipescomp.presentation.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.local.entities.FavoriteRecipesEntity
import com.example.recipescomp.domain.repository.FavoriteRecipeRepository
import com.example.recipescomp.data.remote.Meal
import com.example.recipescomp.presentation.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.presentation.favorites.FavoriteRecipeViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherRecipeSection(
    meal: Meal,
    navController: NavController,
){
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val viewModel: FavoriteRecipeViewModel = viewModel(
        factory = FavoriteRecipeViewModelFactory(repository, currentUserId)
    )
    val favorites by viewModel.favorites.collectAsState()
    val isFavorite = favorites.any { it.name == meal.strMeal }

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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Categoríes: ${meal.strCategory ?: "Unknown"}",
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
                meal.strIngredient10,
                meal.strIngredient11,
                meal.strIngredient12,
                meal.strIngredient13,
                meal.strIngredient14,
                meal.strIngredient15
            ).count { !it.isNullOrBlank() }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ingredients: $ingredientCount",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Country: ${meal.strArea ?: "Unknown"}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }


        //Boton de favoritos
        IconButton(onClick = {
            if (isFavorite) {
                viewModel.deleteFavorite(meal.strMeal)
            } else {
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
                    meal.strIngredient10,
                    meal.strIngredient11,
                    meal.strIngredient12,
                    meal.strIngredient13,
                    meal.strIngredient14,
                    meal.strIngredient15,
                    meal.strIngredient16,
                    meal.strIngredient17,
                    meal.strIngredient18,
                    meal.strIngredient19,
                    meal.strIngredient20
                ).count { !it.isNullOrBlank() }

                viewModel.insertFavorite(
                    FavoriteRecipesEntity(
                        mealId = meal.idMeal,
                        name = meal.strMeal,
                        imageUrl = meal.strMealThumb,
                        category = meal.strCategory,
                        ingredientCount = ingredientCount,
                        area = meal.strArea,
                        userId = currentUserId
                    )
                )
            }


        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }
    }


}



