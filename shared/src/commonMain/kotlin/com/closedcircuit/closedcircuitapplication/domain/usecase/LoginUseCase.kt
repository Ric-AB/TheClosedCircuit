package com.closedcircuit.closedcircuitapplication.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiSuccessResponse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(email: String, password: String): ApiResponse<LoginResponse> {
        var loginResult = userRepository.loginWithEmailAndPassword(email, password)
        loginResult.onSuccess {
            sessionRepository.updateSession(
                lastLogin = it.lastLogin,
                token = it.token,
                firebaseCustomToken = it.firebaseCustomToken,
                fcmServerKey = it.fcmServerKey
            )
            userRepository.getUser(it.userId).onError { code, message ->
                loginResult = ApiErrorResponse(message, code)
            }
        }

        return loginResult
    }
}