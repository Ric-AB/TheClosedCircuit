package com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.AuthenticationState

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