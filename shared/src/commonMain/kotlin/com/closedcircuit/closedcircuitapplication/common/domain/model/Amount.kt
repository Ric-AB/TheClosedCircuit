package com.closedcircuit.closedcircuitapplication.common.domain.model

import com.closedcircuit.closedcircuitapplication.common.presentation.util.formatNumberToCurrency
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Amount(val value: Double) : Comparable<Amount> {

    init {
        require(value >= 0.toDouble()) { "Price ($value) cannot be less than 0.0" }
    }

    fun getFormattedValue(): String {
        return formatNumberToCurrency(value, "NGN")
    }

    operator fun minus(other: Amount): Amount {
        return Amount(value - other.value)
    }

    operator fun div(other: Amount): Amount {
        return if (other.value <= 0.0) Amount(0.0)
        else Amount(value / other.value)
    }

    operator fun plus(other: Amount): Amount {
        return Amount(value + other.value)
    }

    override fun compareTo(other: Amount): Int {
        return compareValuesBy(this, other, Amount::value)
    }
}