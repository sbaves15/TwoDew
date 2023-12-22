package com.twodew.features.todo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.twodew.R
import com.twodew.data.model.Todo
import com.twodew.features.todo.ui.components.AddTodoDialog
import com.twodew.features.todo.ui.components.ErrorMessage
import com.twodew.features.todo.ui.components.LoadingIndicator
import com.twodew.features.todo.ui.components.TodoListContent
import com.twodew.features.todo.ui.model.TodoAction
import com.twodew.features.todo.ui.model.TodoState
import com.twodew.features.todo.ui.model.TodoViewModel
import com.twodew.features.todo.ui.model.UIEvent
import kotlinx.coroutines.launch


@Composable
fun TodoListScreen() {
    val viewModel: TodoViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val editableTodo = remember { mutableStateOf<Todo?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                    .show()

                is UIEvent.ShowErrorToast -> Toast.makeText(
                    context,
                    context.getString(R.string.generic_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add to do")
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (val currentState = state) {
                is TodoState.Loading -> LoadingIndicator()
                is TodoState.Success -> {
                    TodoListContent(
                        todos = currentState.todos,
                        onAction = { action ->
                            if (action is TodoAction.Edit) {
                                editableTodo.value = action.todo
                                showDialog.value = true
                            } else {
                                coroutineScope.launch {
                                    viewModel.handleTodoAction(action)
                                }
                            }
                        },
                    )
                }

                is TodoState.Error -> ErrorMessage()
            }

            if (showDialog.value) {
                AddTodoDialog(
                    onAction = { action ->
                        coroutineScope.launch {
                            viewModel.handleTodoAction(action)
                        }
                    },
                    onClose = {
                        showDialog.value = false
                        editableTodo.value = null
                    },
                    editableTodo = editableTodo.value,
                )
            }
        }

    }


}