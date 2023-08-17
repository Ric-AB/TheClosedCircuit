package com.closedcircuit.closedcircuitapplication.domain.value

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Name(val value: String) {
    init {
        require(value.length >= 2 && value.isNotBlank()) { "Invalid Name- $value" }
    }
}