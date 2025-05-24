package com.example.recipescomp.Navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipescomp.Screems.Inicio_Login
import com.example.recipescomp.Screems.Registrarse
import com.example.recipescomp.Screems.Login_Principal
import com.example.recipescomp.Screems.Principal
import com.example.recipescomp.Screems.Favoritos


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
            Favoritos(navController)
        }
        composable("Principal"){
            Principal(navController)
        }
    }
}