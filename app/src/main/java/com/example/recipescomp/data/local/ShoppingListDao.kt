package com.example.recipescomp.data.local

import androidx.room.*

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    @Query("SELECT * FROM shopping_items")
    suspend fun getAllItems(): List<ShoppingItemEntity>

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)
}
