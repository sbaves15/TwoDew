package com.twodew.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskTitle: String,
    val taskBody: String? = null,
    val isImportant: Boolean,
    val isCompleted: Boolean,
)