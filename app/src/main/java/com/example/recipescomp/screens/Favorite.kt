package com.example.recipescomp.screens

import androidx.compose.foundation.lazy.items
import com.example.recipescomp.resourcesApi.Meal
import com.example.recipescomp.components.BackButton
import com.example.recipescomp.components.BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModelFactory
import com.example.recipescomp.screens.home.OtherRecipeSection
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun ListFavRec(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())
    //Creacion de recetas
    val viewModel: FavoriteRecipeViewModel = viewModel(factory = FavoriteRecipeViewModelFactory(repository))
    val recipes by viewModel.favorites.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 40.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(start = 16.dp, end = 16.dp, bottom = 70.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BackButton(onClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Recetas Favoritas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recipes) { fav ->
                    val fakeMeal = Meal(
                        idMeal = fav.mealId,
                        strMeal = fav.name,
                        strMealThumb = fav.imageUrl ?: "",
                        strCategory = fav.category,
                        strArea = null,
                        strInstructions = null,
                        strTags = null,
                        strYoutube = null,
                        strDrinkAlternate = null,
                        strIngredient1 = null, strMeasure1 = null,
                        strIngredient2 = null, strMeasure2 = null,
                        strIngredient3 = null, strMeasure3 = null,
                        strIngredient4 = null, strMeasure4 = null,
                        strIngredient5 = null, strMeasure5 = null,
                        strIngredient6 = null, strMeasure6 = null,
                        strIngredient7 = null, strMeasure7 = null,
                        strIngredient8 = null, strMeasure8 = null,
                        strIngredient9 = null, strMeasure9 = null,
                        strIngredient10 = null, strMeasure10 = null,
                        strIngredient11 = null, strMeasure11 = null,
                        strIngredient12 = null, strMeasure12 = null,
                        strIngredient13 = null, strMeasure13 = null,
                        strIngredient14 = null, strMeasure14 = null,
                        strIngredient15 = null, strMeasure15 = null,
                        strIngredient16 = null, strMeasure16 = null,
                        strIngredient17 = null, strMeasure17 = null,
                        strIngredient18 = null, strMeasure18 = null,
                        strIngredient19 = null, strMeasure19 = null,
                        strIngredient20 = null, strMeasure20 = null
                    ).apply {
                        // 🔄 Rellenamos dinámicamente los ingredientes simulados
                        try {
                            for (i in 1..fav.ingredientCount.coerceAtMost(20)) {
                                val field = this::class.java.getDeclaredField("strIngredient$i")
                                field.isAccessible = true
                                field.set(this, "Ingrediente $i") // Valor más descriptivo
                            }
                        } catch (e: Exception) {
                            // Manejo de errores en caso de que falle la reflexión
                        }
                    }

                    OtherRecipeSection(
                        meal = fakeMeal,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }

        // 🔽 BARRA DE NAVEGACIÓN INFERIOR
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(50))
                .background(BrownDark)
                .shadow(10.dp, RoundedCornerShape(50))
                .fillMaxWidth()
                .height(64.dp)
        )
    }
}