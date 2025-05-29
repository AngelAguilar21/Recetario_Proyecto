package com.example.recipescomp.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipescomp.screens.Principal
import com.example.recipescomp.screens.Inicio_Login
import com.example.recipescomp.auth.Registrarse
import com.example.recipescomp.auth.Login_Principal
import com.example.recipescomp.screens.*
import com.example.recipescomp.screens.shoppingList.*



@Composable
fun Navigation(){
    val navController = rememberNavController()
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

        composable("search"){
            SearchScreen(navController)
        }
        composable("lista_ingredientes"){
            ListaIngredientes(navController)
        }

        composable("result"){
            ResultScreen(navController)
        }

        composable("search")
        {
            SearchScreen(navController)
        }
    }
}