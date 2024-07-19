package com.closedcircuit.closedcircuitapplication.sponsor.di

import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.account.SponsorAccountTabViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard.SponsorDashboardViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval.StepApprovalViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.FundedPlanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.list.FundedPlanListViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails.LoanDetailsViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loanlist.LoansViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.MakeOfferViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { SponsorAccountTabViewModel(get()) }
    factory { MakeOfferViewModel(get(), get()) }
    factory { SponsorDashboardViewModel(get(), get()) }
    factory { parameters -> LoansViewModel(parameters.get(), get()) }
    factory { parameters -> LoanDetailsViewModel(parameters.get(), get()) }
    factory { FundedPlanListViewModel(get()) }
    factory { parameters -> FundedPlanDetailsViewModel(parameters.get(), get(), get()) }
    factory { parameters ->
        StepApprovalViewModel(
            planID = parameters.get(),
            stepID = parameters.get(),
            canApprove = parameters.get(),
            isStepApprovedByUser = parameters.get(),
            planRepository = get(),
            stepRepository = get(),
            budgetRepository = get(),
            userRepository = get()
        )
    }
}