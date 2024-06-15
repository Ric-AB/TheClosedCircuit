package com.closedcircuit.closedcircuitapplication.common.data.app

import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.extensions.cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class AppSettingsRepositoryImpl(
    private val appSettingsStore: KStore<AppSettings>
) : AppSettingsRepository {

    override suspend fun updateOnboardingState() {
        appSettingsStore.update { currentSettings ->
            currentSettings?.copy(hasOnboarded = true)
        }
    }

    override suspend fun hasOnboarded(): Boolean {
        return appSettingsStore.get()?.hasOnboarded ?: false
    }

    override suspend fun setActiveProfile(profileType: ProfileType) {
        appSettingsStore.update { currentSettings ->
            currentSettings?.copy(activeProfile = profileType)
        }
    }

    override suspend fun getActiveProfile(): ProfileType {
        return appSettingsStore.get()?.activeProfile ?: ProfileType.BENEFICIARY
    }

    override fun getAppSettings(): Flow<AppSettings> {
        return appSettingsStore.updates.mapNotNull { it }
    }
}