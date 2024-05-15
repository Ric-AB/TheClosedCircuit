package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.beneficiary.di.beneficiaryModules
import com.closedcircuit.closedcircuitapplication.sponsor.di.sponsorModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun startAppDI(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    modules(
        networkModule +
                coroutineModule +
                databaseModule +
                platformModule
    )
    modules(beneficiaryModules)
    modules(sponsorModules)
    appDeclaration()
}
