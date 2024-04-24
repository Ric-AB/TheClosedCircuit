package com.closedcircuit.closedcircuitapplication.beneficiary.domain.model

import com.closedcircuit.closedcircuitapplication.common.util.randomUUID
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ID(val value: String) {
    init {
        val regex =
            Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$")
        require(regex.matches(value)) { "Invalid ID- $value" }
    }

    companion object {
        fun generateRandomUUID(): ID {
            return ID(randomUUID())
        }
    }
}