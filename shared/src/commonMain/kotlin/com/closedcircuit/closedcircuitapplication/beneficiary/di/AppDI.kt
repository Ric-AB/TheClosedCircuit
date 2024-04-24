package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.common.di.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun startAppDI(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    modules(
        dataModule +
                viewModelModule +
                networkModule +
                useCaseModule +
                coroutineModule +
                databaseModule +
                platformModule
    )
    appDeclaration()
}