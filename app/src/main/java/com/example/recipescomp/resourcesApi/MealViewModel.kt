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

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Estados para los filtros
    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories: State<List<Category>> = _categories

    private val _areas = mutableStateOf<List<Area>>(emptyList())
    val areas: State<List<Area>> = _areas

    private val _ingredients = mutableStateOf<List<Ingredient>>(emptyList())
    val ingredients: State<List<Ingredient>> = _ingredients

    init {
        fetchMeals()
        fetchCategories()
        fetchAreas()
        fetchIngredients()
    }

    private fun fetchMeals() {
        _isLoading.value = true
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

                val shuffled = allMeals.shuffled()

                withContext(Dispatchers.Main) {
                    _meals.value = shuffled
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                println("Error al obtener recetas: ${e.message}")
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getCategories().execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _categories.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener categorías: ${e.message}")
            }
        }
    }

    private fun fetchAreas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getAreas().execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _areas.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener áreas: ${e.message}")
            }
        }
    }

    private fun fetchIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getIngredients().execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _ingredients.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener ingredientes: ${e.message}")
            }
        }
    }

    fun fetchMealsByCategory(category: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getMealsByCategory(category).execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _meals.value = it
                            _isLoading.value = false
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _meals.value = emptyList()
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener recetas por categoría: ${e.message}")
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun searchMeals(query: String) {
        _isLoading.value = true
        _meals.value = emptyList()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.searchMeals(query).execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _meals.value = it
                            _isLoading.value = false
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            _meals.value = emptyList()
                            _isLoading.value = false
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _meals.value = emptyList()
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                println("Error al realizar la búsqueda: ${e.message}")
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun applyFilters(category: String, area: String, ingredient: String) {
        _isLoading.value = true
        _meals.value = emptyList()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = when {
                    category.isNotEmpty() -> RetrofitClient.api.getMealsByCategory(category).execute()
                    area.isNotEmpty() -> RetrofitClient.api.getMealsByArea(area).execute()
                    ingredient.isNotEmpty() -> RetrofitClient.api.getMealsByIngredient(ingredient).execute()
                    else -> {
                        withContext(Dispatchers.Main) {
                            _isLoading.value = false
                        }
                        return@launch
                    }
                }

                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _meals.value = it
                            _isLoading.value = false
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            _meals.value = emptyList()
                            _isLoading.value = false
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _meals.value = emptyList()
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                println("Error al aplicar filtros: ${e.message}")
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun getRandomMeal() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getRandomMeal().execute()
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        withContext(Dispatchers.Main) {
                            _meals.value = it
                            _isLoading.value = false
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener receta aleatoria: ${e.message}")
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearFilters() {
        _isLoading.value = true
        fetchMeals()
    }
}