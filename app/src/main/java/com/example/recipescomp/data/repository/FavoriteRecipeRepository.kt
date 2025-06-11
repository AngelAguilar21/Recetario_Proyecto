package com.example.recipescomp.data.repository

import com.example.recipescomp.data.local.FavoriteRecipesDao
import com.example.recipescomp.data.local.FavoriteRecipesEntity
import kotlinx.coroutines.flow.Flow

data class FavoriteRecipeRepository(private val dao: FavoriteRecipesDao){

    //Insert
    suspend fun insert(recipe: FavoriteRecipesEntity) = dao.insert(recipe)
    //Delete
    suspend fun delete(id: Int) = dao.delete(id)
    //Delete
    suspend fun delete(recipe: FavoriteRecipesEntity) = dao.delete(recipe)
    //Get all
    fun getAll(): Flow<List<FavoriteRecipesEntity>> = dao.getAll()

    suspend fun deleteByName(name: String) = dao.deleteByName(name)
}
