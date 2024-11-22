package com.closedcircuit.closedcircuitapplication.common.domain.usecase

import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import dev.gitlive.firebase.auth.FirebaseAuth

class IsLoggedInUseCase(
    private val firebaseAuth: FirebaseAuth,
    private val sessionRepository: SessionRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val logoutUseCase: LogoutUseCase
) {

    suspend operator fun invoke(): AuthenticationState {
        val hasOnboarded = appSettingsRepository.hasOnboarded()
        val currentSession = sessionRepository.get()
        return when {
            !hasOnboarded -> AuthenticationState.FIRST_TIME
            hasOnboarded && currentSession == null -> {
                logoutUseCase()
                AuthenticationState.LOGGED_OUT
            }

            currentSession != null -> {
                if (currentSession.hasExpired(firebaseAuth.currentUser)) {
                    logoutUseCase()
                    AuthenticationState.LOGGED_OUT
                } else
                    AuthenticationState.LOGGED_IN
            }

            else -> AuthenticationState.FIRST_TIME
        }
    }
}