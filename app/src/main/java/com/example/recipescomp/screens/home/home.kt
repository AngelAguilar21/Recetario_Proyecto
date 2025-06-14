package com.example.recipescomp.screens.home

import androidx.compose.ui.platform.LocalContext
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModelFactory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipescomp.resourcesApi.MealViewModel
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.ui.theme.BrownDark
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MealViewModel = viewModel()) {
    val meals = viewModel.meals.value
    val shuffledMeals = meals.shuffled()
    val featuredMeals = shuffledMeals.take(10)
    val otherMeals = shuffledMeals.drop(5).take(13)

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val favoriteRepository = FavoriteRecipeRepository(db.FavoriteRecipesDao())
    val favoriteViewModel: FavoriteRecipeViewModel = viewModel(factory = FavoriteRecipeViewModelFactory(favoriteRepository))


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                HeaderSection(navController)
            }
            item {
                FeaturesRecipesSection(navController, featuredMeals)
                Spacer(modifier = Modifier.height(25.dp))
            }
            item {
                CategoriesSection(navController)
                Spacer(modifier = Modifier.height(8.dp))
            }
            item{
                Text(
                    "Otras recetas",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    color = BrownDark,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

            }
            if (otherMeals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else {
                items(otherMeals) { meal ->
                    OtherRecipeSection(
                        meal = meal,
                        navController = navController,
                        viewModel = favoriteViewModel
                    )
                }

            }
        }

        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp) // 🔼 lo eleva del borde inferior
                .padding(horizontal = 32.dp) // 🔼 lo separa de los bordes laterales
                .clip(RoundedCornerShape(50)) // 🟢 redondeado total
                .background(BrownDark)
                .shadow(10.dp, RoundedCornerShape(50)) // ✨ sombra flotante
                .fillMaxWidth()
                .height(64.dp) // 📏 altura fija opcional,
        )
    }
}

