package com.example.todolist

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TodoModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}