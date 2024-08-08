package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.account.AccountTabViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard.DashboardViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc.KycViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.LoansDashboardViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.details.LoanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.loanlist.LoansViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.preview.LoansPreviewViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.completestep.CompleteStepViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan.CreatePlanViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.editplan.EditPlanViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest.FundRequestViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails.PlanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist.PlanListViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep.SaveStepViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails.StepDetailsViewModel
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof.UploadProofViewModel
import com.closedcircuit.closedcircuitapplication.common.di.namedIODispatcher
import com.closedcircuit.closedcircuitapplication.common.di.namedImageStorageReference
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    factory { DashboardViewModel(get(), get(), get()) }
    factory { AccountTabViewModel(get()) }

    // plan management
    factory {
        CreatePlanViewModel(
            createPlanUseCase = get(),
            imageStorageReference = get(named(namedImageStorageReference)),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }

    factory { PlanListViewModel(get()) }
    factory { parameters -> SaveStepViewModel(parameters[0], parameters[1], get(), get()) }
    factory { parameters -> PlanDetailsViewModel(parameters.get(), get(), get(), get()) }
    factory { parameters -> EditPlanViewModel(parameters.get(), get()) }
    factory { parameters -> StepDetailsViewModel(parameters.get(), get(), get()) }
    factory { parameters -> FundRequestViewModel(parameters.get(), get(), get(), get()) }
    factory { parameters ->
        CompleteStepViewModel(
            planID = parameters.get(),
            stepID = parameters.get(),
            planRepository = get(),
            stepRepository = get()
        )
    }
    factory { parameters ->
        UploadProofViewModel(
            budgetID = parameters.get(),
            budgetRepository = get(),
            imageStorageReference = get(named(namedImageStorageReference)),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }

    // kyc
    factory { KycViewModel(get()) }

    // loan
    factory { LoansDashboardViewModel(get()) }
    factory { LoansPreviewViewModel(get()) }
    factory { LoansViewModel(get()) }
    factory { parameters -> LoanDetailsViewModel(parameters.get(), get()) }
}