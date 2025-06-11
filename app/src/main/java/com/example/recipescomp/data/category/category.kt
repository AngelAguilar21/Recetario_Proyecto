package com.example.recipescomp.data.category

import androidx.compose.ui.graphics.vector.ImageVector


data class Category(
    val id: String,
    val name: String,
    val iconComposable: ImageVector, // El ícono de la categoría
    val route: String // Ruta para la navegación
)
