package com.closedcircuit.closedcircuitapplication.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.onboarding.OnboardingScreen

@Composable
internal fun AppNavigation() {
    Navigator(OnboardingScreen)
}