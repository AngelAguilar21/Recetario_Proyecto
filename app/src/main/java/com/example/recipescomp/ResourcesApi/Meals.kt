package com.example.recipescomp.ResourcesApi

data class MealResponse(
    val meals: List<Meal>
)

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strCategory: String?,              // 👈 nueva
    val strIngredient1: String?,          // 👇 estos 3 para contar ingredientes
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?
)

