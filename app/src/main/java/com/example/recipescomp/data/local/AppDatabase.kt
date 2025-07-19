package com.example.recipescomp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipescomp.data.local.dao.FavoriteRecipesDao
import com.example.recipescomp.data.local.dao.ShoppingListDao
import com.example.recipescomp.data.local.entities.FavoriteRecipesEntity
import com.example.recipescomp.data.local.entities.ShoppingItemEntity

@Database(
    entities = [FavoriteRecipesEntity::class, ShoppingItemEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavoriteRecipesDao(): FavoriteRecipesDao
    abstract fun ShoppingListDao(): ShoppingListDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }
}
