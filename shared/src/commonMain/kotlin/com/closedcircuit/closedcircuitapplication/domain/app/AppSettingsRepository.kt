package com.closedcircuit.closedcircuitapplication.domain.app

import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    suspend fun updateOnboardingState()

    fun onboardingStateFlow(): Flow<Boolean>
    suspend fun hasOnboarded(): Boolean
}