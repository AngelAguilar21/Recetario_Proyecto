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

    private val _shuffledMeals = mutableStateOf<List<Meal>>(emptyList())
    val shuffledMeals: State<List<Meal>> = _shuffledMeals


    init {
        fetchMeals()
        fetchCategories()
        fetchAreas()
        fetchIngredients()
    }

    fun fetchMeals() {

        // Evita volver a cargar si ya hay datos
        if (_meals.value.isNotEmpty()) return

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
                    _shuffledMeals.value = shuffled // ← esto guarda la mezcla una sola vez
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

    fun getRandomMeals(cantidad: Int) {
        _isLoading.value = true
        _meals.value = emptyList()

        viewModelScope.launch(Dispatchers.IO) {
            val randomMeals = mutableListOf<Meal>()

            try {
                repeat(cantidad) {
                    val response = RetrofitClient.api.getRandomMeal().execute()
                    if (response.isSuccessful) {
                        response.body()?.meals?.firstOrNull()?.let {
                            randomMeals.add(it)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    _meals.value = randomMeals
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                println("Error al obtener recetas aleatorias: ${e.message}")
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

    // Metodo para obtener una receta por su ID

    private val _selectedMeal = mutableStateOf<Meal?>(null)
    val selectedMeal: State<Meal?> = _selectedMeal

    fun fetchMealById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getMealById(id).execute()
                if (response.isSuccessful) {
                    val meal = response.body()?.meals?.firstOrNull()
                    withContext(Dispatchers.Main) {
                        _selectedMeal.value = meal
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener receta por ID: ${e.message}")
            }
        }
    }

    fun getAllMealsSorted() {
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

                val sorted = allMeals.sortedBy { it.strMeal }

                withContext(Dispatchers.Main) {
                    _meals.value = sorted
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                println("Error al obtener recetas ordenadas: ${e.message}")
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }


}