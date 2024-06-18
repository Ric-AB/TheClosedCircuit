package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.collections.immutable.ImmutableList

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Content(
        val activeProfile: ProfileType,
        val profileTypes: ImmutableList<ProfileType>
    ) : SettingsUiState
}

sealed interface SettingResult {
    data class ChangeActiveProfileSuccess(val newProfile: ProfileType) : SettingResult
}
