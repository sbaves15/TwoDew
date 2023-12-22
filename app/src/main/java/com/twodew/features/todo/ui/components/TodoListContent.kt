package com.twodew.features.todo.ui.components

import TodoItem
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.twodew.data.model.Todo
import com.twodew.features.todo.ui.model.TodoAction

@Composable
fun TodoListContent(
    todos: List<Todo>,
    onAction: (TodoAction) -> Unit,
) {
    LazyColumn {
        items(
            items = todos,
            key = { todo -> todo.id }
        ) { todo ->
            TodoItem(
                todo = todo,
                onAction = onAction,
                modifier = Modifier.padding(PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp)),
            )
        }
    }
}