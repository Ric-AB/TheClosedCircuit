package com.closedcircuit.closedcircuitapplication.common.domain.app

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    suspend fun updateOnboardingState()
    suspend fun hasOnboarded(): Boolean
    suspend fun setActiveProfile(profileType: ProfileType)
    suspend fun getActiveProfile(): ProfileType
    fun getActiveProfileAsFlow(): Flow<ProfileType>
    fun getAppSettings(): Flow<AppSettings>
}