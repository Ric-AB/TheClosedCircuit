package com.closedcircuit.closedcircuitapplication.beneficiary.data.app

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.app.AppSettingsRepository
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

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
        return appSettings.filterNotNull().map {
            it.hasOnboarded
        }
    }

    override suspend fun hasOnboarded(): Boolean {
        return appSettingsStore.get()?.hasOnboarded ?: false
    }
}