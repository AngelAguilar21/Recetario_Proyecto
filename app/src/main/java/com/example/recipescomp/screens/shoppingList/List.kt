package com.example.recipescomp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.shared.ListaDeComprasState
import com.example.recipescomp.shared.Ingrediente
import com.example.recipescomp.ui.theme.BrownDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaIngredientes(navController: NavController) {
    val recetasSeleccionadas = ListaDeComprasState.recetasSeleccionadas

    val ingredientes: List<Ingrediente> = if (recetasSeleccionadas.isNotEmpty()) {
        recetasSeleccionadas
            .flatMap { it.ingredientes }
            .groupBy { it.nombre }
            .map { (nombre, list) ->
                Ingrediente(
                    nombre = nombre,
                    cantidad = list.joinToString(" + ") { it.cantidad }
                )
            }
    } else {
        emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ingredientes a Comprar", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrownDark)
            )
        }
    ) { padding ->
        if (ingredientes.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No se han seleccionado recetas.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(ingredientes) { ingrediente ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2E7))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = ingrediente.nombre, fontSize = 18.sp, color = BrownDark)
                            Text(text = ingrediente.cantidad, fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

