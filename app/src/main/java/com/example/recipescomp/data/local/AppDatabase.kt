package com.example.recipescomp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteRecipesEntity::class], version = 3)
abstract class AppDatabase: RoomDatabase(){
    abstract fun FavoriteRecipesDao(): FavoriteRecipesDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase?=null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration() // 🧨 agrega esta línea
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }
}