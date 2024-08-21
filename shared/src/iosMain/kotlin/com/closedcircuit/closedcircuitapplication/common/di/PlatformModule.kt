package com.closedcircuit.closedcircuitapplication.common.di

import app.cash.sqldelight.db.SqlDriver
import com.closedcircuit.closedcircuitapplication.core.database.DatabaseDriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> { DatabaseDriverFactory().create() }
    single<HttpClientEngine> { Darwin.create() }
}