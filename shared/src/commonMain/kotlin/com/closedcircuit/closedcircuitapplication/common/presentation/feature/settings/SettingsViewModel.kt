package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val appSettingsRepository: AppSettingsRepository) :
    BaseScreenModel<SettingsUiState, SettingResult>() {

    val state = appSettingsRepository.getAppSettings()
        .map {
            SettingsUiState.Content(
                activeProfile = it.activeProfile,
                profileTypes = ProfileType.values().toList().toImmutableList()
            )
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading,
        )

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.ProfileTypeChange -> setActiveProfile(event.profileType)
            is SettingsUiEvent.TogglePrompt -> {}
        }
    }

    private fun setActiveProfile(profileType: ProfileType) {
        val currentProfile = (state.value as? SettingsUiState.Content)?.activeProfile
        if (currentProfile != profileType) {
            screenModelScope.launch {
                appSettingsRepository.setActiveProfile(profileType)
                val newActiveProfile = appSettingsRepository.getActiveProfile()
                _resultChannel.send(SettingResult.ChangeActiveProfileSuccess(newActiveProfile))
            }
        }
    }
}