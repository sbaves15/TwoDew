package com.twodew.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.twodew.data.dao.TodoDao
import com.twodew.data.db.TodoDatabase
import com.twodew.data.model.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest {

    private lateinit var database: TodoDatabase
    private lateinit var todoDao: TodoDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()

        todoDao = database.todoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTodoSavesData() {
        runBlocking {
            val todo = Todo(
                taskTitle = "Test",
                taskBody = "Test Body",
                isImportant = false,
                isCompleted = false
            )
            val insertedId = todoDao.insertTodo(todo)

            val insertedTodo = todoDao.getTodoById(insertedId.toInt())
            assertEquals("Test", insertedTodo.taskTitle)
            assertEquals("Test Body", insertedTodo.taskBody)
            assertEquals(false, insertedTodo.isImportant)
            assertEquals(false, insertedTodo.isCompleted)
        }
    }

    @Test
    fun updateTodoUpdatesData() {
        runBlocking {
            val todo = Todo(
                taskTitle = "Test",
                taskBody = "Test Body",
                isImportant = false,
                isCompleted = false
            )
            val insertedId = todoDao.insertTodo(todo)

            val updatedTodo =
                todo.copy(id = insertedId.toInt(), taskTitle = "Updated Title", isCompleted = true)
            todoDao.updateTodo(updatedTodo)

            val retrievedTodo = todoDao.getTodoById(insertedId.toInt())
            assertEquals("Updated Title", retrievedTodo.taskTitle)
            assertEquals(true, retrievedTodo.isCompleted)
        }
    }

    @Test
    fun deleteTodoRemovesData() {
        runBlocking {
            val todo = Todo(
                taskTitle = "Test",
                taskBody = "Test Body",
                isImportant = false,
                isCompleted = false
            )
            val insertedId = todoDao.insertTodo(todo)

            val todoToDelete = todoDao.getTodoById(insertedId.toInt())
            todoDao.deleteTodo(todoToDelete)

            val retrievedTodo = todoDao.getTodoById(insertedId.toInt())
            assertNull(retrievedTodo)
        }
    }

    @Test
    fun getTodoByIdRetrievesCorrectTodo() {
        runBlocking {
            val todo = Todo(
                taskTitle = "Test",
                taskBody = "Test Body",
                isImportant = false,
                isCompleted = false
            )
            val insertedId = todoDao.insertTodo(todo)

            val retrievedTodo = todoDao.getTodoById(insertedId.toInt())
            assertNotNull(retrievedTodo)
            assertEquals("Test", retrievedTodo.taskTitle)
            assertEquals("Test Body", retrievedTodo.taskBody)
        }
    }

    @Test
    fun getAllTodosRetrievesAllData() {
        runBlocking {
            val todo1 = Todo(
                taskTitle = "Test 1",
                taskBody = "Body 1",
                isImportant = false,
                isCompleted = false
            )
            val todo2 = Todo(
                taskTitle = "Test 2",
                taskBody = "Body 2",
                isImportant = true,
                isCompleted = true
            )
            todoDao.insertTodo(todo1)
            todoDao.insertTodo(todo2)

            val allTodos = todoDao.getAllTodos().first()
            assertEquals(2, allTodos.size)
        }
    }
}