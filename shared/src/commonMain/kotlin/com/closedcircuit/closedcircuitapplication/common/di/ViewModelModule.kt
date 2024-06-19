package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery.ResetPasswordKoinContainer
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery.ResetPasswordViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register.RegisterViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification.NotificationViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding.OnboardingViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.edit.EditProfileViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home.ProfileViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification.ProfileVerificationViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings.SettingsViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { RootViewModel(get(), get(), get()) }

    // onboarding
    factory { OnboardingViewModel(get()) }
    factory { LoginViewModel(get(), get()) }
    factory { RegisterViewModel(get()) }
    single { ResetPasswordKoinContainer() } // todo replace with scoped navigator
    scope<ResetPasswordKoinContainer> {
        scoped { ResetPasswordViewModel(get()) }
    }

    // profile
    factory { ProfileViewModel(get(), get()) }
    factory { parameters -> EditProfileViewModel(parameters.get(), get()) }
    factory { parameters -> ProfileVerificationViewModel(parameters.get(), get()) }

    // notification
    factory { NotificationViewModel(get()) }

    // settings
    factory { SettingsViewModel(get()) }

    // payment
    factory { PaymentViewModel() }
}