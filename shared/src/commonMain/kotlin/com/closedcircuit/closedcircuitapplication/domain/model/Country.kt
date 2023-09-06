package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Country(val value: String) {

    init {
        require(value.isNotBlank()) { "Invalid country- $value" }
    }
}