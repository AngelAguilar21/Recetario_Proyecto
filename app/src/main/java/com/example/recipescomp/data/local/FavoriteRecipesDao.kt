package com.example.recipescomp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipesDao {
    @Query("DELETE FROM favorite_recipes WHERE name = :name")
    suspend fun deleteByName(name: String)  // Cambiado a void

    @Insert
    suspend fun insert(recipes: FavoriteRecipesEntity): Long  // Retorna el ID insertado

    @Query("SELECT * FROM favorite_recipes")
    fun getAll(): Flow<List<FavoriteRecipesEntity>>

    @Query("DELETE FROM favorite_recipes WHERE id = :id")
    suspend fun delete(id: Int): Int  // Cambiado a int, número de filas eliminadas

    @Delete
    suspend fun delete(recipes: FavoriteRecipesEntity)
}
