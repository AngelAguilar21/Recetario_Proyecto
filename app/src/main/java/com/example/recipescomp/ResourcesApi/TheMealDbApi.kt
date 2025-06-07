package com.example.recipescomp.ResourcesApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {
    @GET("search.php")
    fun getMealsByLetter(@Query("f") letter: String): Call<MealResponse>
}
