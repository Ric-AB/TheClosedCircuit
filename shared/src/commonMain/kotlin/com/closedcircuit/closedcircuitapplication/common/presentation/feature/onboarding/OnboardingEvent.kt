package com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding

internal sealed interface OnboardingEvent {
    object OnboardingFinished : OnboardingEvent
}