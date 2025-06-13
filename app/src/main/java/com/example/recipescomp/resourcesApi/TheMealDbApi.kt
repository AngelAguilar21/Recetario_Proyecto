package com.example.recipescomp.resourcesApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {
    // Metodo para buscar recetas por letra
    @GET("search.php")
    fun getMealsByLetter(@Query("f") letter: String): Call<MealResponse>

    // Metodo para buscar recetas por categoría
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealResponse>

    // Metodo para buscar recetas por área/país
    @GET("filter.php")
    fun getMealsByArea(@Query("a") area: String): Call<MealResponse>

    // Metodo para buscar recetas por ingrediente
    @GET("filter.php")
    fun getMealsByIngredient(@Query("i") ingredient: String): Call<MealResponse>

    // Metodo para realizar la búsqueda de recetas por nombre
    @GET("search.php")
    fun searchMeals(@Query("s") query: String): Call<MealResponse>

    // Metodo para obtener una receta aleatoria
    @GET("random.php")
    fun getRandomMeal(): Call<MealResponse>

    // Metodo para obtener todas las categorías
    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryResponse>

    // Metodo para obtener todas las áreas/países
    @GET("list.php?a=list")
    fun getAreas(): Call<AreaResponse>

    // Metodo para obtener todos los ingredientes
    @GET("list.php?i=list")
    fun getIngredients(): Call<IngredientResponse>
}