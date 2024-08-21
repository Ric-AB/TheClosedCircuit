package com.closedcircuit.closedcircuitapplication.common.di

import app.cash.sqldelight.db.SqlDriver
import com.closedcircuit.closedcircuitapplication.core.database.DatabaseDriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver> { DatabaseDriverFactory(androidContext()).create() }
    single<HttpClientEngine> { OkHttp.create() }
}