package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.domain.usecase.LoginUseCase
import com.closedcircuit.closedcircuitapplication.domain.usecase.RegisterUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { LoginUseCase(get(), get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single { IsLoggedInUseCase(get(), get()) }
    single { CreatePlanUseCase(get(), get(), get()) }
}