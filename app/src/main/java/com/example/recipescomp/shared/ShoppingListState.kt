package com.example.recipescomp.shared

import androidx.compose.runtime.mutableStateListOf

// Clase para representar una receta
data class Ingrediente(
    val nombre: String = "",
    val cantidad: String = "" // Ejemplo: "2 tazas"
)

data class Receta(
    val id: Int,
    val nombre: String,
    val imagen: String,
    val ingredientes: List<Ingrediente> = emptyList(), // Se agregan los ingredientes
    var comprada: Boolean = false
)

// Estado compartido para las recetas seleccionadas
object ListaDeComprasState {
    val recetasSeleccionadas = mutableStateListOf<Receta>()

    fun agregarReceta(receta: Receta): Boolean {
        // Evitar duplicados por ID
        return if (recetasSeleccionadas.none { it.id == receta.id }) {
            recetasSeleccionadas.add(receta)
            true
        } else {
            false
        }
    }
}