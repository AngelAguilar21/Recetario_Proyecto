package com.example.recipescomp.presentation.navegation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipescomp.data.Firebase.FirebaseAuthManager
import com.example.recipescomp.presentation.home.HomeScreen
import com.example.recipescomp.presentation.auth.Inicio_Login
import com.example.recipescomp.presentation.auth.Registrarse
import com.example.recipescomp.presentation.auth.Login_Principal
import com.example.recipescomp.presentation.home.SearchScreen
import com.example.recipescomp.viewmodel.MealViewModel
import com.example.recipescomp.presentation.category.CategoryScreen
import com.example.recipescomp.presentation.favorites.ListFavRec
import com.example.recipescomp.presentation.kitchenmode.Modo_Cocina
import com.example.recipescomp.presentation.profile.Perfil
import com.example.recipescomp.presentation.recipe.Receta
import com.example.recipescomp.presentation.shoppingList.Lista_Compras
import com.example.recipescomp.presentation.shoppingList.SummaryListScreen


@Composable
fun Navigation(){
    val navController = rememberNavController()
    val mealViewModel: MealViewModel = viewModel()
    // Validar si el usuario está logueado
    val startDestination = if (FirebaseAuthManager.isUserLoggedIn()) {
        "Principal"
    } else {
        "Inicio_Login"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("Inicio_Login"){
            Inicio_Login(navController)
        }
        composable("Registrarse"){
            Registrarse(navController)
        }
        composable("Login_Principal"){
            Login_Principal(navController)
        }
        composable("Favoritos"){
            ListFavRec(navController)
        }
        composable("Principal"){
            HomeScreen(navController, mealViewModel)
        }
        composable("listaCompras"){
            Lista_Compras(navController)
        }

        composable("receta/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            val selectedMeal = mealViewModel.selectedMeal.value // Changed: Use .value instead of by

            // Cargar los detalles completos de la receta
            LaunchedEffect(mealId) {
                if (mealId.isNotEmpty()) {
                    mealViewModel.fetchMealById(mealId)
                }
            }

            if (selectedMeal != null) {
                Receta(navController, selectedMeal)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        composable("perfil"){
            Perfil(navController)
        }

        composable(
            "modoCocina/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: return@composable
            Modo_Cocina(navController, mealId)
        }
        composable("summary_list") {
            SummaryListScreen(navController = navController)
        }

        composable("search") {
            SearchScreen(navController, mealViewModel)
        }
        composable("category/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: return@composable
            CategoryScreen(category, navController)
        }
    }
}