package com.closedcircuit.closedcircuitapplication.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun startAppDI(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    modules(
        dataModule + viewModelModule + networkModule + useCaseModule + coroutineModule
    )
    appDeclaration()
}