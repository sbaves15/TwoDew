package com.twodew.features.todo.ui.model

sealed interface UIEvent {
    data class ShowToast(val message: String) : UIEvent
    data object ShowErrorToast : UIEvent
}