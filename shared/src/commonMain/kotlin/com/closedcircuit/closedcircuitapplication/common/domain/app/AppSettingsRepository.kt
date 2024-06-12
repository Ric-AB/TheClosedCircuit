package com.closedcircuit.closedcircuitapplication.common.domain.app

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    suspend fun updateOnboardingState()
    fun onboardingStateFlow(): Flow<Boolean>
    suspend fun hasOnboarded(): Boolean
    suspend fun setActiveProfile(profileType: ProfileType)
    fun getActiveProfile(): Flow<ProfileType>
}