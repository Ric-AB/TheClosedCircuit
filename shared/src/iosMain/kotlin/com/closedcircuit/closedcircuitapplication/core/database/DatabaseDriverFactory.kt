package com.closedcircuit.closedcircuitapplication.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            TheClosedCircuitDatabase.Schema,
            "the_closed_circuit.db"
        )
    }
}