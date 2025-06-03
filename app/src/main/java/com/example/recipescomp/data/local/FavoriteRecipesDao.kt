package com.example.recipescomp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipesDao {
    @Query("DELETE FROM favorite_recipes WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Insert
    suspend fun insert(recipes: FavoriteRecipesEntity)

    @Query("SELECT * FROM favorite_recipes")
    fun getAll(): Flow<List<FavoriteRecipesEntity>>

    @Query("DELETE FROM favorite_recipes WHERE id = :id")
    suspend fun delete(id: Int)

    @Delete
    suspend fun delete(recipes: FavoriteRecipesEntity)
}