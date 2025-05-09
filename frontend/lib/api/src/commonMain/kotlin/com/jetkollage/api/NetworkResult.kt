package com.jetkollage.api

sealed class NetworkResult<out T> {

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class ServerError<T>(val httpCode: Int) : NetworkResult<T>()
    data class Error<T>(val exception: Throwable) : NetworkResult<T>()

    fun dataOrNull(): T? = if (this is Success) data else null
}