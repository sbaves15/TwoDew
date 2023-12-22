package com.twodew.features.todo.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twodew.data.model.Todo
import com.twodew.data.repository.api.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository,
) : ViewModel() {

    // set up a backing variable for our TodoState with loading as default
    private val _state = MutableStateFlow<TodoState>(TodoState.Loading)
    val state: StateFlow<TodoState> = _state.asStateFlow()

    // same setup for ui events (which are just showing toasts for this project)
    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadTodos()
    }

    internal fun handleTodoAction(action: TodoAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (action) {
                is TodoAction.Create -> createTodo(action.todo)
                is TodoAction.Update -> updateTodo(action.todo)
                is TodoAction.Delete -> deleteTodo(action.todo)
                is TodoAction.ToggleCompleteStatus -> toggleCompleteStatus(action.todo)
                is TodoAction.ToggleImportantStatus -> toggleImportantStatus(action.todo)
                is TodoAction.Edit -> updateTodo(action.todo, "edited")
            }
        }
    }

    private fun loadTodos() = viewModelScope.launch {
        try {
            _state.value = TodoState.Loading
            repository.getAllTodos().collect { todos ->
                _state.value = TodoState.Success(todos)
            }
        } catch (e: Exception) {
            _state.value = TodoState.Error(message = e.message ?: "Unknown error")
        }
    }

    private suspend fun createTodo(todo: Todo) {
        try {
            repository.insertTodo(todo)
            _uiEvent.emit(UIEvent.ShowToast("${todo.taskTitle} added"))
        } catch (e: Exception) {
            //Log.e("TodoViewModel", "Error creating todo", e)
            _uiEvent.emit(UIEvent.ShowErrorToast)
        }
    }

    private suspend fun updateTodo(todo: Todo, modification: String = "updated") {
        try {
            repository.updateTodo(todo = todo)
            _uiEvent.emit(UIEvent.ShowToast("${todo.taskTitle} $modification"))
        } catch (e: Exception) {
            //Log.e("TodoViewModel", "Error updating todo", e)
            _uiEvent.emit(UIEvent.ShowErrorToast)
        }

    }

    private suspend fun deleteTodo(todo: Todo) {
        try {
            repository.deleteTodo(todo = todo)
            _uiEvent.emit(UIEvent.ShowToast("${todo.taskTitle} deleted"))
        } catch (e: Exception) {
            //Log.e("TodoViewModel", "Error updating todo")
            _uiEvent.emit(UIEvent.ShowErrorToast)
        }
    }

    private suspend fun toggleCompleteStatus(todo: Todo) {
        val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
        val modification = if (updatedTodo.isCompleted) "marked completed" else "marked uncompleted"
        updateTodo(updatedTodo, modification)
    }

    private suspend fun toggleImportantStatus(todo: Todo) {
        val updatedTodo = todo.copy(isImportant = !todo.isImportant)
        val modification = if (updatedTodo.isImportant) "marked important" else "marked unimportant"
        updateTodo(updatedTodo, modification)
    }
}