package com.closedcircuit.closedcircuitapplication.common.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Currency(val value: String) {
    init {
        require(value.isNotBlank()) { "Invalid currency- $value" }
    }
}

fun Currency?.orDefault() = this ?: Currency("USD")