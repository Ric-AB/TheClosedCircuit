package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Currency(val value: String) {
    init {
        require(value.isNotBlank()) { "Invalid currency- $value" }
    }
}