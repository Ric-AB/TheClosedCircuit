package com.closedcircuit.closedcircuitapplication.common.di

import app.cash.sqldelight.db.SqlDriver
import com.closedcircuit.closedcircuitapplication.core.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver> { DatabaseDriverFactory(androidContext()).create() }
}