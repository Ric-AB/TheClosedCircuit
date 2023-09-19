package com.closedcircuit.closedcircuitapplication.di

import app.cash.sqldelight.db.SqlDriver
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<TheClosedCircuitDatabase> { createDatabase(get()) }
}

fun createDatabase(driver: SqlDriver): TheClosedCircuitDatabase {
    return TheClosedCircuitDatabase(driver = driver)
}