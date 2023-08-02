package com.closedcircuit.closedcircuitapplication.android

import android.app.Application
import com.closedcircuit.closedcircuitapplication.di.startAppDI
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class TheClosedCircuitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupLogging()
    }

    private fun setupKoin() {
        startAppDI {
            androidLogger()
            androidContext(this@TheClosedCircuitApplication)
        }
    }

    private fun setupLogging() {
        Napier.base(DebugAntilog())
    }
}