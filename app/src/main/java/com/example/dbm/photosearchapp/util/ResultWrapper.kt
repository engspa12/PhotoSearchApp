package com.example.dbm.photosearchapp.util

sealed class ResultWrapper<out T,out U>{
    data class Success<T>(val value: T): ResultWrapper<T, Nothing>()
    data class Failure<U>(val exception: Exception? = null, val error: U? = null): ResultWrapper<Nothing, U>()
}