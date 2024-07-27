package com.closedcircuit.closedcircuitapplication.core.network

import com.closedcircuit.closedcircuitapplication.core.network.dto.Envelope


sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiResponse<T> {
            return ApiErrorResponse(
                errorMessage = error.message ?: "Unknown error",
                httpStatusCode = 0
            )
        }

        fun <T> create(envelope: Envelope<T>): ApiResponse<T> {
            return ApiSuccessResponse(envelope.data!!)
        }
    }
}

data class ApiSuccessResponse<T>(
    val body: T
) : ApiResponse<T>()

/**
 * Separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(
    val errorMessage: String,
    val httpStatusCode: Int
) : ApiResponse<T>()

val <T> ApiResponse<T>.isSuccessful: Boolean
    get() = this is ApiSuccessResponse<T>

val <T> ApiResponse<T>.isError: Boolean
    get() = this is ApiErrorResponse<T>

fun <T> ApiResponse<T>.getOrNull(): T? {
    return if (this is ApiSuccessResponse<T>) this.body
    else null
}

suspend fun <T : Any> ApiResponse<T>.onComplete(
    executable: suspend () -> Unit
): ApiResponse<T> = apply {
    executable()
}

suspend fun <T : Any> ApiResponse<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiSuccessResponse<T>) {
        executable(body)
    }
}

suspend fun <T, R> ApiResponse<T>.mapOnSuccess(
    transform: suspend (T) -> R
): ApiResponse<R> {
    return when (this) {
        is ApiSuccessResponse<T> -> ApiSuccessResponse(transform(body))
        is ApiErrorResponse<T> -> ApiErrorResponse(errorMessage, httpStatusCode)
        else -> ApiEmptyResponse()
    }
}


suspend fun <T : Any> ApiResponse<T>.onError(
    executable: suspend (code: Int, message: String) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiErrorResponse) {
        executable(httpStatusCode, errorMessage)
    }
}