package com.example.recipescomp.presentation.home

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
import com.example.recipescomp.viewmodel.MealViewModel
import com.example.recipescomp.core.components.BottomNavigationBar
import com.example.recipescomp.core.theme.BrownDark
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.domain.repository.FavoriteRecipeRepository
import com.example.recipescomp.presentation.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.presentation.favorites.FavoriteRecipeViewModelFactory
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MealViewModel) {
    val shuffledMeals = viewModel.shuffledMeals.value
    val featuredMeals = shuffledMeals.take(10)
    val otherMeals = shuffledMeals.drop(5).take(13)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())

    val viewModel: FavoriteRecipeViewModel = viewModel(
        factory = FavoriteRecipeViewModelFactory(repository, currentUserId)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())

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
                    "Other Recipes",
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
                        CircularProgressIndicator(color = BrownDark)
                    }
                }
            } else {
                items(otherMeals) { meal ->
                    OtherRecipeSection(
                        meal = meal,
                        navController = navController,
                        viewModel = viewModel
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

