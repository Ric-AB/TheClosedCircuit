package com.closedcircuit.closedcircuitapplication.domain.value

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class RawDate(val value: String) {
    init {
        val regex =
            Regex("([0-9]{4})-(1[0-2]|0[1-9]|[1-9])-(3[01]|[12][0-9]|0[1-9])T[0-9][0-9]:(0?[0-5][0-9]|[0-9]):([0-5][0-9])$")
        require(regex.matches(value)) { "Invalid date- $value" }
    }
}