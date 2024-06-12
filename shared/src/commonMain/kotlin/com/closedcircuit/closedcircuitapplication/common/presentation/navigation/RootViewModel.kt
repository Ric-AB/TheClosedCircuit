package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.coroutines.launch

class RootViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : ScreenModel {

    val activeProfile = appSettingsRepository.getActiveProfile()

    init {
        observe()
    }

    private fun observe() {
        screenModelScope.launch {
            activeProfile.collect {
                println("#### active $it")
            }
        }
    }

    fun update() {
        screenModelScope.launch {
            appSettingsRepository.setActiveProfile(ProfileType.SPONSOR)
        }
    }
}