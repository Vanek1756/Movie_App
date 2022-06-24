package com.example.movie.util

import java.net.ConnectException
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T> safeApiCall(
    call: suspend () -> T,
    success: (response: T) -> Unit,
    error: (exception: ApiException) -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            val response = call.invoke()

            if (response is Response<*> && response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    success(response)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            val exception = when (e) {
                is ConnectException,
                is UnknownHostException -> ApiException.NetworkException(cause = e)
                is ApiException.HttpException, is HttpException -> {
                    val errorCode =
                        (e as? ApiException.HttpException)?.errorCode ?: (e as HttpException).code()

                    when (errorCode) {
                        in 400..499 -> ApiException.HttpException.ClientException(
                            e.message,
                            errorCode,
                            e
                        )
                        in 500..599 -> ApiException.HttpException.ServerException(
                            errorCode = errorCode,
                            cause = e
                        )
                        else -> ApiException.HttpException(errorCode = errorCode, cause = e)
                    }
                }
                else -> ApiException.SomethingException(cause = e)
            }
            withContext(Dispatchers.Main) {
                error(exception)
            }
        }
    }
}
