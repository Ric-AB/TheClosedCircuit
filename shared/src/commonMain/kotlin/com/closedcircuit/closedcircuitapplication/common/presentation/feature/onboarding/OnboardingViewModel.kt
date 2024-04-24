package com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.app.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class OnboardingViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : ScreenModel {


    fun onEvent(onboardingEvent: OnboardingEvent) {
        when (onboardingEvent) {
            OnboardingEvent.OnboardingFinished -> {
                screenModelScope.launch(Dispatchers.Main) {
                    appSettingsRepository.updateOnboardingState()
                }
            }
        }
    }
}