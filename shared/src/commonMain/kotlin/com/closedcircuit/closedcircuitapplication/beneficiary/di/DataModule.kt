package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.BudgetRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.donation.DonationRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.FundRequestRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.LoanRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.PlanRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.StepRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.wallet.WalletRepositoryImpl
import com.closedcircuit.closedcircuitapplication.common.di.namedDefaultDispatcher
import com.closedcircuit.closedcircuitapplication.common.di.namedIODispatcher
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.wallet.WalletRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module


val dataModule = module {
    single<WalletRepository> { WalletRepositoryImpl() }
    single<PlanRepository> {
        PlanRepositoryImpl(
            database = get(),
            planService = get(),
            stepRepository = get(),
            budgetRepository = get(),
            ioDispatcher = get(named(namedDefaultDispatcher)),
            defaultDispatcher = get(named(namedDefaultDispatcher))
        )
    }

    single<StepRepository> {
        StepRepositoryImpl(
            database = get(),
            stepService = get(),
            ioDispatcher = get(named(namedIODispatcher)),
            defaultDispatcher = get(named(namedDefaultDispatcher))
        )
    }

    single<BudgetRepository> {
        BudgetRepositoryImpl(
            database = get(),
            budgetService = get(),
            ioDispatcher = get(named(namedIODispatcher)),
            defaultDispatcher = get(named(namedDefaultDispatcher))
        )
    }

    single<DonationRepository> {
        DonationRepositoryImpl(
            donationService = get(),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }

    single<FundRequestRepository> {
        FundRequestRepositoryImpl(
            service = get(),
            theClosedCircuitDatabase = get()
        )
    }
    single<LoanRepository> {
        LoanRepositoryImpl(
            loanService = get(),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }
}