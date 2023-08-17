package com.closedcircuit.closedcircuitapplication.domain.value

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Avatar(val value: String) {
    init {
        val regex = Regex(
            "((http|https)://)(www.)?” + " +
                    "[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]" +
                    "{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)"
        )

        require(regex.matches(value)) { "Invalid URL- $value" }
    }
}