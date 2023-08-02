package com.closedcircuit.closedcircuitapplication

import com.closedcircuit.closedcircuitapplication.di.startAppDI
import org.koin.core.context.startKoin

fun initKoin() {
    startAppDI()
}