package com.closedcircuit.closedcircuitapplication.onboarding

sealed interface OnboardingEvent {
    object OnboardingFinished : OnboardingEvent
}