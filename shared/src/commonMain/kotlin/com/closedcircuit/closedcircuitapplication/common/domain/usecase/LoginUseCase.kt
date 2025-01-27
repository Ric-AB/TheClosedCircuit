package com.closedcircuit.closedcircuitapplication.common.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.common.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): ApiResponse<LoginResponse> {
        var loginResult = authenticationRepository.loginWithEmailAndPassword(email, password)
        loginResult.onSuccess {
            sessionRepository.updateSession(
                lastLogin = it.lastLogin,
                token = it.token,
                firebaseCustomToken = it.firebaseCustomToken,
                fcmServerKey = it.fcmServerKey
            )

            userRepository.fetchLoggedInUser(ID(it.userId)).onError { code, message ->
                loginResult = ApiErrorResponse(message, code)
            }
        }

        return loginResult
    }
}