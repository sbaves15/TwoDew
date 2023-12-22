package com.twodew.data.repository.impl

import com.twodew.data.dao.TodoDao
import com.twodew.data.model.Todo
import com.twodew.data.repository.api.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao,
) : TodoRepository {

    override suspend fun insertTodo(todo: Todo): Long = dao.insertTodo(todo)

    override suspend fun updateTodo(todo: Todo) = dao.updateTodo(todo)

    override suspend fun deleteTodo(todo: Todo) = dao.deleteTodo(todo)

    override fun getAllTodos(): Flow<List<Todo>> = dao.getAllTodos()
}