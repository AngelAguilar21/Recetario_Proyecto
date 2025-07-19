package com.example.recipescomp.domain.repository

import com.example.recipescomp.data.local.dao.FavoriteRecipesDao
import com.example.recipescomp.data.local.entities.FavoriteRecipesEntity
import kotlinx.coroutines.flow.Flow

data class FavoriteRecipeRepository(private val dao: FavoriteRecipesDao){

    //Insert
    suspend fun insert(recipe: FavoriteRecipesEntity) = dao.insert(recipe)

    fun getFavoritesByUser(userId: String): Flow<List<FavoriteRecipesEntity>> =
        dao.getFavoritesByUserFlow(userId)

    suspend fun deleteByNameAndUser(name: String, userId: String) =
        dao.deleteByNameAndUser(name, userId)

}
