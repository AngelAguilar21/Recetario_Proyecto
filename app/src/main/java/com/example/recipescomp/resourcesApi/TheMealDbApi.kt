package com.example.recipescomp.resourcesApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {
    @GET("search.php")
    fun getMealsByLetter(@Query("f") letter: String): Call<MealResponse>

    // Metodo adicional para buscar por categoría
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealResponse>

    // Metodo para realizar la búsqueda de recetas por nombre
    @GET("search.php")
    fun searchMeals(@Query("s") query: String): Call<MealResponse>
}
