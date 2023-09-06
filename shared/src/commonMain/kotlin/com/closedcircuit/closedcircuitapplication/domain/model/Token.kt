package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Token(val value: String) {
    init {
        require(value.isNotBlank()) { "Invalid token representation- $value" }
    }
}