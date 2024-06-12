package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { RootViewModel(get()) }
}