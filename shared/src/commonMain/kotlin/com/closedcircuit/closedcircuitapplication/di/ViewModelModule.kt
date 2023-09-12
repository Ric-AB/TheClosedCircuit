package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery.ResetPasswordKoinContainer
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery.ResetPasswordViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register.RegisterViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard.DashboardViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding.OnboardingViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home.ProfileViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit.EditProfileViewModel
import org.koin.dsl.module

val viewModelModule = module {
    // onboarding
    factory { OnboardingViewModel(get()) }
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    single { ResetPasswordKoinContainer() }
    scope<ResetPasswordKoinContainer> {
        scoped { ResetPasswordViewModel(get()) }
    }

    factory { DashboardViewModel(get(), get(), get(), get(), get()) }
    factory { ProfileViewModel(get(), get()) }
    factory { parameters -> EditProfileViewModel(parameters.get(), get()) }
}