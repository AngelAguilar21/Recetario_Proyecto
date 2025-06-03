package com.example.recipescomp.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipescomp.data.repository.FavoriteRecipeRepository

data class FavoriteRecipeViewModelFactory(private val repository: FavoriteRecipeRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteRecipeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavoriteRecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
