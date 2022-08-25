package com.sevdotdev.wowson.ui.model

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sevdotdev.wowson.ui.model.UiState.Error.fold


sealed class UiState<out T> {

    inline fun <R, reified T> UiState<T>.fold(
        isSuccess: (value: T) -> R,
        isLoading: () -> R,
        isError: () -> R,
    ): R = when (this) {
        is Success<T> -> {
            isSuccess(this.get())
        }
        Error -> {
            isError()
        }
        Loading -> {
            isLoading()
        }
    }

    data class Success<out T>(private val viewState: T) : UiState<T>() {
        fun get(): T = viewState
    }

    object Loading : UiState<Nothing>()
    object Error : UiState<Nothing>()
}

@Composable
inline fun <R, reified D, T : UiState<D>> UiStateContentView(
    modifier: Modifier = Modifier.fillMaxSize(),
    state: T,
    loadingContent: @Composable () -> R,
    errorContent: @Composable (message: String?) -> R,
    content: @Composable (viewState: D) -> R,
): R = state.fold(
    isError = {
        errorContent(null)
    },
    isLoading = {
        loadingContent()
    },
    isSuccess = {
        content(it)
    }
)