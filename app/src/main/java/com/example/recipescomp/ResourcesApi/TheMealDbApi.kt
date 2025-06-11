package com.example.recipescomp.ResourcesApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {
    @GET("search.php")
    fun getMealsByLetter(@Query("f") letter: String): Call<MealResponse>

    // Método adicional para buscar por categoría
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealResponse>
}
