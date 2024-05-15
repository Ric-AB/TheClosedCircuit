package com.closedcircuit.closedcircuitapplication.common.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val namedIODispatcher = "IODispatcher"
const val namedDefaultDispatcher = "DefaultDispatcher"
val coroutineModule = module {
    single(named(namedIODispatcher)) { Dispatchers.IO }
    single(named(namedDefaultDispatcher)) { Dispatchers.Default }
}