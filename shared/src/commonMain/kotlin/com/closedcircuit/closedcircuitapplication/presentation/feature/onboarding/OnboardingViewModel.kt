package com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.domain.app.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class OnboardingViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : ScreenModel {


    fun onEvent(onboardingEvent: OnboardingEvent) {
        when (onboardingEvent) {
            OnboardingEvent.OnboardingFinished -> {
                coroutineScope.launch(Dispatchers.Main) {
                    appSettingsRepository.updateOnboardingState()
                }
            }
        }
    }
}