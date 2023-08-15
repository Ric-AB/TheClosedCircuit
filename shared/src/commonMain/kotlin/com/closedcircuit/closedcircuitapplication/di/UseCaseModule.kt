package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.domain.usecase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { LoginUseCase(get(), get()) }
}