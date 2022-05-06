package com.someparking.androidapp.domain

sealed class RepositoryResult <out T: Any?>{
    class Success<out T : Any?>(val data: T) : RepositoryResult<T>()
    class Error(val exception: Throwable) : RepositoryResult<Nothing>()
}