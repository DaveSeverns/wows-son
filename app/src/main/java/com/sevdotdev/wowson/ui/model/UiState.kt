package com.sevdotdev.wowson.ui.model


sealed class UiState<out T> {
    data class Success<out T>(private val viewState: T) : UiState<T>() {
        fun get(): T = viewState
    }
    object Loading : UiState<Nothing>()
    object Error : UiState<Nothing>()
}