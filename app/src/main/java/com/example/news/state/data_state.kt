package com.example.news.state

sealed interface DataState<out T> {
    open class Success<T>(val data: T) : DataState<T>
    open class Error(val message: String) : DataState<Nothing>
}