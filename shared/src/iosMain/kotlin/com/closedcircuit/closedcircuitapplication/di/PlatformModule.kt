package com.closedcircuit.closedcircuitapplication.di

import app.cash.sqldelight.db.SqlDriver
import com.closedcircuit.closedcircuitapplication.core.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> { DatabaseDriverFactory().create() }
}