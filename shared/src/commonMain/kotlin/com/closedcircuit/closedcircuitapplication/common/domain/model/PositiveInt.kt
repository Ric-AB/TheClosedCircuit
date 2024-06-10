package com.closedcircuit.closedcircuitapplication.common.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class PositiveInt(val value: Int) {
    operator fun minus(other: PositiveInt): PositiveInt {
        return PositiveInt(value - other.value)
    }

    operator fun div(other: PositiveInt): PositiveInt {
        return if (other.value <= 0) PositiveInt(0)
        else PositiveInt(value / other.value)
    }

    operator fun plus(other: PositiveInt): PositiveInt {
        return PositiveInt(value + other.value)
    }
}