package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Price(val value: String) {
    constructor(value: Double) : this(value.toString())

    init {
        require(value.toDouble() > 0.toDouble()) { "Price ($value) cannot be less than 0.0" }
    }
}