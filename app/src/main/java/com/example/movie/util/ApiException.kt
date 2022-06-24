package com.example.movie.util

sealed class ApiException(
    override val message: String?,
    var code: Int = 0,
    cause: Throwable? = null
) : Exception(message, cause) {

    class NetworkException(
        message: String? = null,
        cause: Throwable? = null
    ) : ApiException(message, 0, cause)

    open class HttpException(
        message: String? = null,
        val errorCode: Int,
        cause: Throwable? = null
    ) : ApiException(message, errorCode, cause) {

        class ClientException(
            message: String? = null,
            errorCode: Int,
            cause: Throwable? = null
        ) : HttpException(message, errorCode, cause)

        class ServerException(
            message: String? = null,
            errorCode: Int,
            cause: Throwable? = null
        ) : HttpException(message, errorCode, cause)
    }

    class SomethingException(
        message: String? = null,
        cause: Throwable? = null
    ) : ApiException(message, 0, cause)
}
