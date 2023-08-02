package com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding

internal sealed interface OnboardingEvent {
    object OnboardingFinished : OnboardingEvent
}