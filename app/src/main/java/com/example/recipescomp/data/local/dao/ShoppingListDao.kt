package com.example.recipescomp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.recipescomp.data.local.entities.ShoppingItemEntity

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: ShoppingItemEntity): Long

    @Query("SELECT * FROM shopping_items WHERE userId = :userId ORDER BY quantity DESC, name ASC")
    suspend fun getAllItems(userId: String): List<ShoppingItemEntity>

    @Query("SELECT * FROM shopping_items WHERE mealId = :mealId AND userId = :userId LIMIT 1")
    suspend fun getItemByMealId(mealId: String, userId: String): ShoppingItemEntity?

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items WHERE userId = :userId")
    suspend fun clearUserItems(userId: String)
}