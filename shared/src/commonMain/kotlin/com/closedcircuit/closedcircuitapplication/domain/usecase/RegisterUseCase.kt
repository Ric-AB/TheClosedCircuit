package com.closedcircuit.closedcircuitapplication.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val loginUseCase: LoginUseCase
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        roles: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<Unit> {
        var registerResult =
            userRepository.register(fullName, email, roles, phoneNumber, password, confirmPassword)
        registerResult.onSuccess {
            loginUseCase(email, password).onError { code, message ->
                registerResult = ApiErrorResponse(message, code)
            }
        }

        return registerResult
    }
}