package com.example.recipescomp.data.category

import androidx.compose.runtime.Composable

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String? = null
)

data class Category(
    val id: String,
    val name: String,
    val iconComposable: @Composable () -> Unit
)