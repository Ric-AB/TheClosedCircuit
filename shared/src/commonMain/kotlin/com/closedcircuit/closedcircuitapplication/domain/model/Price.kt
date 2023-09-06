package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Price(val value: String) {

    init {
        require(value.toFloat() > 0F) { "Price ($value) cannot be less than 0.0" }
    }
}