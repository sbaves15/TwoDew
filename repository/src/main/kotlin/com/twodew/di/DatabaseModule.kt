package com.twodew.di

import android.content.Context
import androidx.room.Room
import com.twodew.data.dao.TodoDao
import com.twodew.data.db.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = TodoDatabase::class.java,
            name = "local_db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }
}