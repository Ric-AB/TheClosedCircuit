package com.closedcircuit.closedcircuitapplication.beneficiary.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Name(val value: String) {
    init {
        require(value.length >= 2 && value.isNotBlank()) { "Invalid Name- $value" }
    }
}