package com.example.recipescomp.Navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipescomp.Screems.Inicio_Login
import com.example.recipescomp.Auth.Registrarse
import com.example.recipescomp.Auth.Login_Principal
import com.example.recipescomp.Screems.Principal
import com.example.recipescomp.Screems.Favoritos
import com.example.recipescomp.Screems.Lista_Compras
import com.example.recipescomp.Screems.Receta
import com.example.recipescomp.Screems.Perfil
import com.example.recipescomp.Screems.Configuracion
import com.example.recipescomp.Screems.Lista_Compras_Detallado
import com.example.recipescomp.Screems.Modo_Cocina


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
        composable("listaCompras"){
            Lista_Compras(navController)
        }
        composable("listaComprasDetallada"){
            Lista_Compras_Detallado(navController)
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
    }
}

