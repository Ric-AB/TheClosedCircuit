package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType

sealed interface SettingsUiEvent {
    data class TogglePrompt(val enabled: Boolean) : SettingsUiEvent
    data class ProfileTypeChange(val profileType: ProfileType) : SettingsUiEvent
    object DeleteAccount : SettingsUiEvent
}