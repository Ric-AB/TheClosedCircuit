package com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings

import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.LogoutUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val appSettingsRepository: AppSettingsRepository,
    private val userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase
) : BaseScreenModel<SettingsUiState, SettingResult>() {

    private val loadingFlow = MutableStateFlow(false)
    val state = appSettingsRepository.getAppSettings()
        .combine(loadingFlow) { settings, loading ->
            SettingsUiState.Content(
                activeProfile = settings.activeProfile,
                profileTypes = ProfileType.values().toList().toImmutableList(),
                loading = loading
            )
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading,
        )

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.ProfileTypeChange -> setActiveProfile(event.profileType)
            is SettingsUiEvent.TogglePrompt -> {}
            SettingsUiEvent.DeleteAccount -> {}
        }
    }

    private fun deleteAccount() {
        screenModelScope.launch {
            loadingFlow.update { true }
            userRepository.deleteAccount()
                .onComplete { loadingFlow.update { false } }
                .onSuccess {
                    logoutUseCase()
                    _resultChannel.send(SettingResult.DeleteAccountSuccess)
                }.onError { _, message ->
                    _resultChannel.send(SettingResult.DeleteAccountError(message))
                }
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