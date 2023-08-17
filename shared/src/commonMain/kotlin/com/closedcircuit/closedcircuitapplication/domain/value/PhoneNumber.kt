package com.closedcircuit.closedcircuitapplication.domain.value

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class PhoneNumber(val value: String) {
    init {
        val regex = Regex(
            "^[+](?:[0-9\\-\\(\\)\\/" +
                    "\\.]\\s?){6,15}[0-9]{1}$"
        )

        require(regex.matches(value)) { "Invalid phone number- $value" }
    }
}