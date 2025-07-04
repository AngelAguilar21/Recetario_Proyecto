package com.example.recipescomp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey val mealId: String,
    val name: String,
    val imageUrl: String,
    val ingredients: String,
    val quantity: Int,
    val userId: String
)
