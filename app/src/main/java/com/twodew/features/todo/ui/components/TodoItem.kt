import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.twodew.data.model.Todo
import com.twodew.features.todo.ui.model.TodoAction

@Composable
fun TodoItem(
    todo: Todo,
    onAction: (TodoAction) -> Unit,
    modifier: Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .clickable { onAction(TodoAction.Edit(todo)) },
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = {
                        onAction(TodoAction.ToggleCompleteStatus(todo))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    val currentStyle = LocalTextStyle.current
                    val style = if (todo.isCompleted) {
                        currentStyle.copy(textDecoration = TextDecoration.LineThrough)
                    } else currentStyle
                    Text(
                        text = todo.taskTitle,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = style,
                        maxLines = 1,
                    )

                    if (todo.taskBody != null) {
                        Text(
                            text = todo.taskBody!!,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = style,
                            maxLines = 3,
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row {
                        IconButton(onClick = {
                            onAction(TodoAction.ToggleImportantStatus(todo))
                        }) {
                            // giving a very obvious color on purpose
                            if (todo.isImportant) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Mark as not important",
                                    tint = Color.Yellow,
                                )
                            } else {
                                // no time to debug but Icons.Outlined.Star doesn't work
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Mark as important",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        IconButton(onClick = {
                            onAction(TodoAction.Edit(todo))
                        }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                        }

                        IconButton(onClick = {
                            onAction(TodoAction.Delete(todo))
                        })
                        {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }


            }
        }
    }
}