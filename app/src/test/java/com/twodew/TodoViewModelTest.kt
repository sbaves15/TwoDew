package com.twodew

import com.twodew.data.model.Todo
import com.twodew.data.repository.api.TodoRepository
import com.twodew.features.todo.ui.model.TodoAction
import com.twodew.features.todo.ui.model.TodoState
import com.twodew.features.todo.ui.model.TodoViewModel
import com.twodew.features.todo.ui.model.UIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

// with more time I'd adjust these tests to use Turbine, and also solve flakiness (when run at once, there are intermittent failures but they pass individually)
// probably a refactor would be that the vm should take in a coroutine dispatcher that defaults to IO, and we can set to main in tests to simplify

class TodoViewModelTest {

    private lateinit var viewModel: TodoViewModel
    private val repository: TodoRepository = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockTodoList = listOf(
        Todo(
            id = 1,
            taskTitle = "Test Todo 1",
            taskBody = "This is the first test todo",
            isImportant = true,
            isCompleted = false
        ),
        Todo(
            id = 2,
            taskTitle = "Test Todo 2",
            taskBody = "This is the second test todo",
            isImportant = false,
            isCompleted = true
        )
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        Mockito.reset(repository)
        whenever(repository.getAllTodos()).thenReturn(flowOf(mockTodoList))
        viewModel = createViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(repository: TodoRepository): TodoViewModel {
        return TodoViewModel(
            repository = repository
        )
    }

    @Test
    fun `todos are loaded on init`() {
        viewModel = createViewModel(repository)

        runTest(testDispatcher) {
            val state = viewModel.state.value

            assertTrue(state is TodoState.Success && state.todos == mockTodoList)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `create todo calls repository with correct task`() {
        viewModel = createViewModel(repository)
        runTest(testDispatcher) {
            val newTodo = Todo(
                taskTitle = "New todo",
                taskBody = "Body",
                isImportant = true,
                isCompleted = false,
            )

            viewModel.handleTodoAction(TodoAction.Create(newTodo))

            advanceUntilIdle()

            verify(repository).insertTodo(newTodo)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update todo calls repository with correct task`() {
        viewModel = createViewModel(repository)
        runTest(testDispatcher) {
            val updatedTodo = mockTodoList.first().copy(taskTitle = "Updated Title")

            viewModel.handleTodoAction(TodoAction.Update(updatedTodo))

            advanceUntilIdle()

            verify(repository).updateTodo(updatedTodo)
        }
    }

    @Test
    fun `delete todo calls repository with correct task`() {
        viewModel = createViewModel(repository)
        runTest(testDispatcher) {
            val todoToDelete = mockTodoList.first()

            viewModel.handleTodoAction(TodoAction.Delete(todoToDelete))

            verify(repository).deleteTodo(todoToDelete)
        }
    }

    @Test
    fun `toggle complete status updates task completion`() {
        viewModel = createViewModel(repository)
        runTest(testDispatcher) {
            val todo = mockTodoList.first()
            val expectedUpdatedTodo = todo.copy(isCompleted = !todo.isCompleted)

            viewModel.handleTodoAction(TodoAction.ToggleCompleteStatus(todo))

            verify(repository).updateTodo(expectedUpdatedTodo)
        }
    }

    @Test
    fun `toggle important status updates task importance`() {
        viewModel = createViewModel(repository)
        runTest(testDispatcher) {
            val todo = mockTodoList.first()
            val expectedUpdatedTodo = todo.copy(isImportant = !todo.isImportant)

            viewModel.handleTodoAction(TodoAction.ToggleImportantStatus(todo))

            verify(repository).updateTodo(expectedUpdatedTodo)
        }
    }

    @Test
    fun `handle error when creating todo`() {
        viewModel = createViewModel(repository)
        runTest(testDispatcher) {
            val todo = Todo(
                taskTitle = "Ahhhh",
                taskBody = null,
                isCompleted = false,
                isImportant = false,
            )

            // Setup the mock to throw an exception
            whenever(repository.insertTodo(todo)).thenAnswer {
                throw RuntimeException("Database write error")
            }

            viewModel.handleTodoAction(TodoAction.Create(todo))

            // Collect the first UI event
            val event = viewModel.uiEvent.first()
            assertTrue(event is UIEvent.ShowErrorToast)

            // Optionally verify that the repository was called
            verify(repository).insertTodo(todo)
        }
    }
}