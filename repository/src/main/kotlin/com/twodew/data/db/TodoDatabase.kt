package com.twodew.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.twodew.data.dao.TodoDao
import com.twodew.data.model.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = true)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}