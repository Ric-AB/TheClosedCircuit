package com.closedcircuit.closedcircuitapplication.core.database

import app.cash.sqldelight.ColumnAdapter

object FloatColumnAdapter : ColumnAdapter<Float, Long> {

    override fun decode(databaseValue: Long): Float {
        return databaseValue.toFloat()
    }

    override fun encode(value: Float): Long {
        return value.toLong()
    }
}