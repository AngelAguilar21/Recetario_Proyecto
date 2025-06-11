package com.example.recipescomp.navegation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipescomp.resourcesApi.MealViewModel
import com.example.recipescomp.screens.home.HomeScreen
import com.example.recipescomp.screens.Inicio_Login
import com.example.recipescomp.auth.Registrarse
import com.example.recipescomp.auth.Login_Principal
import com.example.recipescomp.screens.*
import com.example.recipescomp.screens.category.CategoryScreen
import com.example.recipescomp.screens.home.SearchScreen
import com.example.recipescomp.screens.shoppingList.*



@Composable
fun Navigation(){
    val navController = rememberNavController()
    val mealViewModel: MealViewModel = viewModel()

    NavHost(navController = navController, startDestination = "Inicio_Login"){
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
            HomeScreen(navController)
        }
        composable("listaCompras"){
            Lista_Compras(navController)
        }
        composable("category/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Breakfast"
            CategoryScreen(category = categoryName, navController = navController)
        }
        composable("receta/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            val meal = mealViewModel.meals.value.find { it.idMeal == mealId }

            if (meal != null) {
                Receta(navController, meal)
            } else {
                // Puedes mostrar un indicador de carga o un mensaje de error aquí
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        composable("perfil"){
            Perfil(navController)
        }
        composable("configuracion"){
            Configuracion(navController)
        }
        composable("modoCocina"){
            Modo_Cocina(navController)
        }

        composable("search") {
            SearchScreen(navController, mealViewModel)
        }
        composable("lista_ingredientes"){
            ListaIngredientes(navController)
        }

    }
}