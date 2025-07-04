package com.example.recipescomp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipesDao {

    @Query("SELECT * FROM favorite_recipes WHERE userId = :userId")
    suspend fun getFavoritesByUser(userId: String): List<FavoriteRecipesEntity>

    @Query("SELECT * FROM favorite_recipes WHERE userId = :userId")
    fun getFavoritesByUserFlow(userId: String): Flow<List<FavoriteRecipesEntity>>

    @Query("DELETE FROM favorite_recipes WHERE name = :name AND userId = :userId")
    suspend fun deleteByNameAndUser(name: String, userId: String)

    @Query("DELETE FROM favorite_recipes WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Insert
    suspend fun insert(recipes: FavoriteRecipesEntity): Long

    @Query("SELECT * FROM favorite_recipes")
    fun getAll(): Flow<List<FavoriteRecipesEntity>>

    @Query("DELETE FROM favorite_recipes WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Delete
    suspend fun delete(recipes: FavoriteRecipesEntity)
}
