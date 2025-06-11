package com.example.recipescomp.resourcesApi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealViewModel : ViewModel() {
    private val _meals = mutableStateOf<List<Meal>>(emptyList())
    val meals: State<List<Meal>> = _meals

    init {
        fetchMeals()
    }

    private fun fetchMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            val allMeals = mutableListOf<Meal>()

            try {
                for (letter in 'a'..'z') {
                    val response = RetrofitClient.api.getMealsByLetter(letter.toString()).execute()
                    if (response.isSuccessful) {
                        response.body()?.meals?.let {
                            allMeals.addAll(it)
                        }
                    }
                }

                val shuffled = allMeals.shuffled() // aleatoriza los resultados

                withContext(Dispatchers.Main) {
                    _meals.value = shuffled
                }
            } catch (e: Exception) {
                println("Error al obtener recetas: ${e.message}")
            }
        }
    }
    fun fetchMealsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Hacer la solicitud a la API para obtener recetas por categoría
                val response = RetrofitClient.api.getMealsByCategory(category).execute()

                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, obtenemos las recetas y las asignamos al estado
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _meals.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener recetas: ${e.message}")
                // En caso de error, puedes manejarlo aquí
            }
        }
    }
    fun searchMeals(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.searchMeals(query).execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        _meals.value = it // Asigna una lista de Meal al estado
                    }
                }
            } catch (e: Exception) {
                println("Error al realizar la búsqueda: ${e.message}")
            }
        }
    }
}