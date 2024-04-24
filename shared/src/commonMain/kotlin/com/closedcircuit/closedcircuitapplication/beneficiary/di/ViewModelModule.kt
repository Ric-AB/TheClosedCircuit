package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard.DashboardViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc.KycViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.details.LoanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.loanlist.LoansViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.preview.LoansPreviewViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.editplan.EditPlanViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest.FundRequestViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails.PlanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist.PlanListViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep.SaveStepViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails.StepDetailsViewModel
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
import org.koin.dsl.module

val viewModelModule = module {
    // onboarding
    factory { OnboardingViewModel(get()) }
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    single { ResetPasswordKoinContainer() } // todo replace with scoped navigator
    scope<ResetPasswordKoinContainer> {
        scoped { ResetPasswordViewModel(get()) }
    }

    factory { DashboardViewModel(get(), get(), get()) }

    // profile
    factory { ProfileViewModel(get(), get()) }
    factory { parameters -> EditProfileViewModel(parameters.get(), get()) }
    factory { parameters -> ProfileVerificationViewModel(parameters.get(), get()) }

    // plan management
    factory { PlanListViewModel(get()) }
    factory { parameters -> SaveStepViewModel(parameters[0], parameters[1], get(), get()) }
    factory { parameters -> PlanDetailsViewModel(parameters.get(), get(), get(), get()) }
    factory { parameters -> EditPlanViewModel(parameters.get(), get()) }
    factory { parameters -> StepDetailsViewModel(parameters.get(), get(), get()) }
    factory { parameters -> FundRequestViewModel(parameters.get(), get(), get(), get()) }


    factory { NotificationViewModel(get()) }

    // kyc
    factory { KycViewModel(get()) }

    // payment
    factory { PaymentViewModel() }

    // loan
    factory { LoansPreviewViewModel(get()) }
    factory { LoansViewModel(get()) }
    factory { parameters -> LoanDetailsViewModel(parameters.get(), get()) }
}