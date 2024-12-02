package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.collections.immutable.ImmutableList

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Content(
        val activeProfile: ProfileType,
        val profileTypes: ImmutableList<ProfileType>,
        val loading: Boolean
    ) : SettingsUiState
}

sealed interface SettingResult {
    data class ChangeActiveProfileSuccess(val newProfile: ProfileType) : SettingResult
    data class DeleteAccountError(val message: String) : SettingResult
    object DeleteAccountSuccess : SettingResult
}
