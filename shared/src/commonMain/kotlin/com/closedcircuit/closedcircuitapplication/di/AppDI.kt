package com.closedcircuit.closedcircuitapplication.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

var appSettingsStorage = ""
fun startAppDI(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    modules(
        dataModule + viewModelModule + networkModule
    )
    appDeclaration()
}