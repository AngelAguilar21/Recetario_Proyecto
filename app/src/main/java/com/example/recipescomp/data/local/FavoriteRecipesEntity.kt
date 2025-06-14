package com.example.recipescomp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: String,
    val name: String,
    val imageUrl: String? = null,
    val category: String? = null,
    val ingredientCount: Int = 0
)
