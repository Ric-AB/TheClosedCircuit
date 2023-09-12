package com.closedcircuit.closedcircuitapplication.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val namedIODispatcher = "IODispatcher"
val coroutineModule = module {
    single(named(namedIODispatcher)) {
        Dispatchers.IO
    }
}