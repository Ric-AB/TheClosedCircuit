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

        fun <T> create(response: Response<Envelope<T>>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body.data!!)
                }
            } else {
                ApiErrorResponse(
                    errorMessage = response.description,
                    httpStatusCode = response.code
                )
            }
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


suspend fun <T : Any> ApiResponse<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiSuccessResponse<T>) {
        executable(body)
    }
}


suspend fun <T : Any> ApiResponse<T>.onError(
    executable: suspend (code: Int, message: String) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiErrorResponse) {
        executable(httpStatusCode, errorMessage)
    }
}