package com.closedcircuit.closedcircuitapplication.data.app

import com.closedcircuit.closedcircuitapplication.data.datasource.local.appSettingsStore
import com.closedcircuit.closedcircuitapplication.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.domain.app.AppSettingsRepository
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class AppSettingsRepositoryImpl(
    appSettingsStore: KStore<AppSettings>
) : AppSettingsRepository {

    private val appSettings = appSettingsStore.updates

    override suspend fun updateOnboardingState() {
        appSettingsStore.update { currentSettings ->
            currentSettings?.copy(shouldShowOnboarding = false)
        }
    }

    override fun onboardingStateFlow(): Flow<Boolean> {
        return appSettings.filterNotNull().map {
            it.shouldShowOnboarding
        }
    }

    override suspend fun onboardingState(): Boolean {
        return appSettingsStore.get()?.shouldShowOnboarding ?: true
    }
}