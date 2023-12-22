package com.twodew.features.todo.ui.model

import com.twodew.data.model.Todo

sealed interface TodoAction {
    data class Create(val todo: Todo) : TodoAction
    data class Update(val todo: Todo) : TodoAction
    data class Delete(val todo: Todo) : TodoAction
    data class ToggleCompleteStatus(val todo: Todo) : TodoAction
    data class ToggleImportantStatus(val todo: Todo) : TodoAction
    data class Edit(val todo: Todo) : TodoAction
}