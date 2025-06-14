package com.example.recipescomp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: String,
    val name: String,
    val imageUrl: String,
    val ingredients: String
)
