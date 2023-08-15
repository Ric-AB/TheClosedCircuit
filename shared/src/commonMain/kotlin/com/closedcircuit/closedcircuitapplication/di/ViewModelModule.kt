package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register.RegisterViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding.OnboardingViewModel
import org.koin.dsl.module

val viewModelModule = module {
    // onboarding
    factory { OnboardingViewModel(appSettingsRepository = get()) }
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel() }
}