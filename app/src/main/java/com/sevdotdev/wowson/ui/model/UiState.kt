package com.sevdotdev.wowson.ui.model

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sevdotdev.wowson.ui.model.UiState.Loading.fold


sealed class UiState<out T> {

    inline fun <R, reified T> UiState<T>.fold(
        isSuccess: (value: T) -> R,
        isLoading: () -> R,
        isError: (throwable: Throwable) -> R,
    ): R = when (this) {
        is Success<T> -> {
            isSuccess(viewState)
        }
        is Error -> {
            isError(exception)
        }
        Loading -> {
            isLoading()
        }
    }

    data class Success<out T>(val viewState: T) : UiState<T>()

    object Loading : UiState<Nothing>()
    data class Error<Nothing>(val exception: Throwable) : UiState<Nothing>()
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
        errorContent(it.message)
    },
    isLoading = {
        loadingContent()
    },
    isSuccess = {
        content(it)
    }
)