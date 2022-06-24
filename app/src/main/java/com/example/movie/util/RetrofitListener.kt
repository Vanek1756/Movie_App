package com.example.movie.util

interface RetrofitListener {

    fun onSuccess()

    fun onError(throwable: Throwable)
}
