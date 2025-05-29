package com.example.recipescomp.screens.shoppingList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Lista_Compras(navController: NavController){

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ){
        Text(text = "Lista de Compras", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("listaComprasDetallada")
            }
        ) {
            Text(text = "Navegar a Lista de Compras Detallado", fontSize = 20.sp)
        }
    }
}


