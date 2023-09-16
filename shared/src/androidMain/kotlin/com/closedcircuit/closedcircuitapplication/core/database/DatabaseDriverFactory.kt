package com.closedcircuit.closedcircuitapplication.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase

actual class DatabaseDriverFactory(
    private val context: Context
) {

    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            TheClosedCircuitDatabase.Schema,
            context,
            "the_closed_circuit.db"
        )
    }
}