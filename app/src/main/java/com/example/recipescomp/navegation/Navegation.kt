package com.example.recipescomp.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipescomp.auth.*
import com.example.recipescomp.screens.*
import com.example.recipescomp.screens.category.*
import com.example.recipescomp.screens.shoppingList.*



@Composable
fun Navegation(){

    var navController = rememberNavController()
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
            Principal(navController)
        }
        composable("listaCompras"){
            Lista_Compras(navController)
        }
        composable("receta"){
            Receta(navController)
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
        composable("recetasDestacadas") {
            RecetasDestacadas(navController)
        }
        composable("desayuno"){
            Breakfast(navController)
        }
        composable("almuerzo"){
            Lunch(navController)
        }
        composable("saludable"){
            Healthy(navController)
        }
        composable("postre"){
            Dessert(navController)
        }
        composable("bebida"){
            Drink(navController)
        }
   }
}

