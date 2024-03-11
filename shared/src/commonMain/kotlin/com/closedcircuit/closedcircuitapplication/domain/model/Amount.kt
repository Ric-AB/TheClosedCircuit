package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Amount(val value: Double) {

    init {
        require(value >= 0.toDouble()) { "Price ($value) cannot be less than 0.0" }
    }

    operator fun minus(other: Amount): Amount {
        return Amount(value - other.value)
    }

    operator fun div(other: Amount): Amount {
        return if (other.value <= 0.0) Amount(0.0)
        else Amount(value / other.value)
    }

    operator fun plus(other: Amount): Amount {
        return Amount(value + other.value)
    }
}