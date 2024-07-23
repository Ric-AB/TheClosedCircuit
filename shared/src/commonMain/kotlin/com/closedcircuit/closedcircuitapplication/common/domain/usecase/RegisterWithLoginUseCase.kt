package com.closedcircuit.closedcircuitapplication.common.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.auth.AuthenticationRepository

class RegisterWithLoginUseCase(
    private val authRepository: AuthenticationRepository,
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
        var registerResult = authRepository.register(
            fullName = fullName,
            email = email,
            roles = roles,
            phoneNumber = phoneNumber,
            password = password,
            confirmPassword = confirmPassword
        )

        registerResult.onSuccess {
            loginUseCase(email, password).onError { code, message ->
                registerResult = ApiErrorResponse(message, code)
            }
        }

        return registerResult
    }
}