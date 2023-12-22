package com.twodew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.twodew.features.todo.ui.screens.TodoListScreen
import com.twodew.design.theme.TwoDewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwoDewTheme {
                TodoListScreen()
            }
        }
    }
}