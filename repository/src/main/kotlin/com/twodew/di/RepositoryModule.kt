package com.twodew.di

import com.twodew.data.repository.api.TodoRepository
import com.twodew.data.repository.impl.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideTodoRepository(
        todoRepositoryImpl: TodoRepositoryImpl,
    ): TodoRepository
}