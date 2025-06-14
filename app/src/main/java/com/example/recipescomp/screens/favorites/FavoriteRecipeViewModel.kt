package com.example.recipescomp.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipescomp.data.local.FavoriteRecipesEntity
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoriteRecipeViewModel(private val repository: FavoriteRecipeRepository): ViewModel(){
    val favorites: StateFlow<List<FavoriteRecipesEntity>> =
        repository
            .getAll()
            .stateIn(viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList())

    fun insertFavorite(recipe: FavoriteRecipesEntity){
        viewModelScope.launch {
            val current = favorites.value
            if (current.none { it.name == recipe.name }) {
                repository.insert(recipe)
            }
        }
    }

    fun deleteFavorite(name: String) {
        viewModelScope.launch {
            repository.deleteByName(name)
        }
    }
    fun deleteFavorite(recipe: FavoriteRecipesEntity){
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }

}
