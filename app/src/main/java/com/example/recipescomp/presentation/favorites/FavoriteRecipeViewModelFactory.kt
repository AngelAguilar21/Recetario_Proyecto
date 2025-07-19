package com.example.recipescomp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipescomp.domain.repository.FavoriteRecipeRepository

class FavoriteRecipeViewModelFactory(
    private val repository: FavoriteRecipeRepository,
    private val userId: String
): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteRecipeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavoriteRecipeViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}