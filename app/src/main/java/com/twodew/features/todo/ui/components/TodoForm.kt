package com.twodew.features.todo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.twodew.data.model.Todo
import com.twodew.features.todo.ui.model.TodoAction

@Composable
fun AddTodoDialog(
    onAction: (TodoAction) -> Unit,
    onClose: () -> Unit,
    editableTodo: Todo?,
) {
    // Local state to store the input values
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var isImportant by remember { mutableStateOf(false) }
    val dialogTitle = if (editableTodo != null) "Edit Todo" else "Add Todo"

    editableTodo?.let {
        title = editableTodo.taskTitle
        body = editableTodo.taskBody ?: ""
        isImportant = editableTodo.isImportant
    }

    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
                    .wrapContentHeight()
            ) {
                Text(
                    text = dialogTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                // Input fields for title and body
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title") },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(text = "Body (optional)") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                // Checkbox for importance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Important?",
                        modifier = Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Checkbox(
                        checked = isImportant,
                        onCheckedChange = { isImportant = it },
                    )
                }

                // Buttons for submitting and closing the dialog
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            // Clear input fields and close the dialog without adding a todo
                            title = ""
                            body = ""
                            isImportant = false
                            onClose()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            val newTodo = Todo(
                                taskTitle = title,
                                taskBody = body.takeIf { it.isNotBlank() },
                                isImportant = isImportant,
                                isCompleted = false,
                            )
                            if (editableTodo != null) {
                                onAction(TodoAction.Edit(newTodo.copy(id = editableTodo.id)))
                            } else {
                                onAction(TodoAction.Create(newTodo))
                            }

                            // Clear input fields and close the dialog
                            title = ""
                            body = ""
                            isImportant = false
                            onClose()
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}