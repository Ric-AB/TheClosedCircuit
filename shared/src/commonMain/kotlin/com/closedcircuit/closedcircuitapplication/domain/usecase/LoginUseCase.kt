package com.closedcircuit.closedcircuitapplication.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        val loginResult = userRepository.loginWithEmailAndPassword(email, password)
        loginResult.onSuccess {
            sessionRepository.updateSession(
                lastLogin = it.lastLogin,
                token = it.token,
                firebaseCustomToken = it.firebaseCustomToken,
                fcmServerKey = it.fcmServerKey
            )
            userRepository.getUser(it.userId)
        }
    }
}