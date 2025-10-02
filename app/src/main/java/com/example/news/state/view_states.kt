package com.example.news.state

sealed interface ViewState<out T> {
    object Loading : ViewState<Nothing>
    object Init : ViewState<Nothing>
    open class Success<T>(val data: T) : ViewState<T>
    open class Error(val message: String) : ViewState<Nothing>
}