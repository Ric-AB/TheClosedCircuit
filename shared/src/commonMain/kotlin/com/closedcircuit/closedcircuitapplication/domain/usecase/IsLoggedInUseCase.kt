package com.closedcircuit.closedcircuitapplication.domain.usecase

import com.closedcircuit.closedcircuitapplication.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.presentation.navigation.AuthenticationState

class IsLoggedInUseCase(
    private val sessionRepository: SessionRepository,
    private val appSettingsRepository: AppSettingsRepository
) {

    suspend operator fun invoke(): AuthenticationState {
        val hasOnboarded = appSettingsRepository.hasOnboarded()
        val currentSession = sessionRepository.get()
        return when {
            currentSession != null -> currentSession.currentAuthenticationState(hasOnboarded)
            hasOnboarded -> AuthenticationState.LOGGED_OUT
            else -> AuthenticationState.FIRST_TIME
        }
    }
}