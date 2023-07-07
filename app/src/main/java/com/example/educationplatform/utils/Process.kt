package com.example.educationplatform.utils

sealed class Process<out T> {
    data class CommonError(val message: String): Process<Nothing>()
    data class InternetError(val message: String): Process<Nothing>()
    object Loading: Process<Nothing>()
    data class Success<out T>(val data: T): Process<T>()
}
