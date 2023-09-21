package com.closedcircuit.closedcircuitapplication.domain.model

import com.closedcircuit.closedcircuitapplication.util.format
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.random.Random

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
            val random = Random

            val firstPart = String.format("%08x", random.nextLong(0x100000000))
            val secondPart = String.format("%04x", random.nextInt(0x10000))
            val thirdPart = String.format("%04x", random.nextInt(0x10000))
            val fourthPart = String.format("%04x", random.nextInt(0x10000))
            val fifthPart = String.format("%012x", random.nextLong())

            return ID("$firstPart-$secondPart-$thirdPart-$fourthPart-$fifthPart")
        }
    }
}