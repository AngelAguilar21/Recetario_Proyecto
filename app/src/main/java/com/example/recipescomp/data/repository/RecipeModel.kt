package com.example.recipescomp.data.repository

data class RecipeModel(val name: String,
                       val imageUrl: String? = null,
                       val ingredients: List<String> = emptyList(),
                        val category: String,



)
