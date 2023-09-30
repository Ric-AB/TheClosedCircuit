package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Price(val value: Double) {

    init {
        require(value >= 0.toDouble()) { "Price ($value) cannot be less than 0.0" }
    }

    operator fun minus(other: Price): Price {
        return Price(value - other.value)
    }

    operator fun div(other: Price): Price {
        return if (other.value <= 0.0) Price(0.0)
        else Price(value / other.value)
    }

    operator fun plus(other: Price): Price {
        return Price(value + other.value)
    }
}