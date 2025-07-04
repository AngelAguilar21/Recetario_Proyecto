package com.example.recipescomp.data.local

import androidx.room.*

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: ShoppingItemEntity): Long

    @Query("SELECT * FROM shopping_items ORDER BY quantity DESC, name ASC")
    suspend fun getAllItems(): List<ShoppingItemEntity>

    @Query("SELECT * FROM shopping_items WHERE mealId = :mealId LIMIT 1")
    suspend fun getItemByMealId(mealId: String): ShoppingItemEntity?

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)
}
