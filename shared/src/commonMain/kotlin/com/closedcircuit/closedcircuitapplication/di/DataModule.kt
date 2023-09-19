package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.core.storage.appSettingsStore
import com.closedcircuit.closedcircuitapplication.core.storage.sessionStore
import com.closedcircuit.closedcircuitapplication.core.storage.userStore
import com.closedcircuit.closedcircuitapplication.data.app.AppSettingsRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.auth.AuthenticationRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.budget.BudgetRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.donation.DonationRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.plan.PlanRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.session.SessionRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.step.StepRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.user.UserRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.wallet.WalletRepositoryImpl
import com.closedcircuit.closedcircuitapplication.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.domain.wallet.WalletRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module


val dataModule = module {
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(appSettingsStore) }
    single<SessionRepository> { SessionRepositoryImpl(sessionStore) }
    single<UserRepository> { UserRepositoryImpl(get(), userStore, get(named(namedIODispatcher))) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
    single<WalletRepository> { WalletRepositoryImpl() }
    single<PlanRepository> {
        PlanRepositoryImpl(
            database = get(),
            planService = get(),
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
    single<DonationRepository> { DonationRepositoryImpl() }
}