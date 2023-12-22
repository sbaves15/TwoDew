package com.twodew.features.todo.ui.model

import com.twodew.data.model.Todo

sealed interface TodoState {
    data object Loading : TodoState
    data class Success(val todos: List<Todo>) : TodoState
    data class Error(val message: String) : TodoState
}