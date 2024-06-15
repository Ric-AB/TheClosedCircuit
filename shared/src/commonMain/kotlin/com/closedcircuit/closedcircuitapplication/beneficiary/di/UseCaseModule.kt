package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.LoginUseCase
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.RegisterUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { LoginUseCase(get(), get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single { IsLoggedInUseCase(get(), get()) }
    single { CreatePlanUseCase(get(), get(), get()) }
}