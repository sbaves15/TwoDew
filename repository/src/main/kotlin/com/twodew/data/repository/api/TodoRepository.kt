package com.twodew.data.repository.api

import com.twodew.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insertTodo(todo: Todo): Long
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    fun getAllTodos(): Flow<List<Todo>>
}