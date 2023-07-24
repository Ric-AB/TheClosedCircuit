package com.closedcircuit.closedcircuitapplication.onboarding

import cafe.adriel.voyager.core.model.ScreenModel

class OnboardingViewModel: ScreenModel {


    fun onEvent(onboardingEvent: OnboardingEvent) {
        when(onboardingEvent) {
            OnboardingEvent.OnboardingFinished -> {}
        }
    }
}