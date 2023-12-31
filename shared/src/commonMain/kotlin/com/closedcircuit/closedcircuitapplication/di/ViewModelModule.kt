package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.passwordrecovery.ResetPasswordKoinContainer
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.passwordrecovery.ResetPasswordViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register.RegisterViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard.DashboardViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.notification.NotificationViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding.OnboardingViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.editplan.EditPlanViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails.PlanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.planlist.PlanListViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep.SaveStepViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.stepdetails.StepDetailsViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit.EditProfileViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home.ProfileViewModel
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.profileverification.ProfileVerificationViewModel
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

    // profile
    factory { ProfileViewModel(get(), get()) }
    factory { parameters -> EditProfileViewModel(parameters.get(), get()) }
    factory { parameters -> ProfileVerificationViewModel(parameters.get(), get()) }

    // plan management
    factory { PlanListViewModel(get()) }
    factory { parameters -> SaveStepViewModel(parameters.get(), parameters.get(), get(), get()) }
    factory { parameters -> PlanDetailsViewModel(parameters.get(), get(), get(), get()) }
    factory { parameters -> EditPlanViewModel(parameters.get(), get()) }
    factory { parameters -> StepDetailsViewModel(parameters.get(), get(), get()) }
    factory { NotificationViewModel(get()) }
}