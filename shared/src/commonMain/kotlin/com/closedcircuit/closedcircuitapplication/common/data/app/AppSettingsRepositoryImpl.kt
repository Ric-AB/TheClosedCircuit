package com.closedcircuit.closedcircuitapplication.common.data.app

import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class AppSettingsRepositoryImpl(
    private val appSettingsStore: KStore<AppSettings>
) : AppSettingsRepository {

    private val appSettings = appSettingsStore.updates

    override suspend fun updateOnboardingState() {
        appSettingsStore.update { currentSettings ->
            currentSettings?.copy(hasOnboarded = true)
        }
    }

    override fun onboardingStateFlow(): Flow<Boolean> {
        return appSettings.mapNotNull { it?.hasOnboarded }
    }

    override suspend fun hasOnboarded(): Boolean {
        return appSettingsStore.get()?.hasOnboarded ?: false
    }

    override suspend fun setActiveProfile(profileType: ProfileType) {
        appSettingsStore.update { currentSettings ->
            currentSettings?.copy(activeProfile = profileType)
        }
    }

    override fun getActiveProfile(): Flow<ProfileType> {
        return appSettings.mapNotNull { it?.activeProfile }
    }
}