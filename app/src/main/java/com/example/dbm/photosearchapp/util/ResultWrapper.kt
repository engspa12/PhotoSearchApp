package com.example.dbm.photosearchapp.util

sealed class ResultWrapper<T>{
    data class Success<T>(val value: T): ResultWrapper<T>()
    data class Failure<T>(val exception: Exception? = null,
                          val errorMessage: MessageWrapper = MessageWrapper()): ResultWrapper<T>()
}