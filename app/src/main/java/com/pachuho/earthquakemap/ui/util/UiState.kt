package com.pachuho.earthquakemap.ui.util

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val error: Throwable?): UiState<Nothing>()
}

fun <T> UiState<T>.successOrNull(): T? = if (this is UiState.Success<T>) {
    data
} else {
    null
}