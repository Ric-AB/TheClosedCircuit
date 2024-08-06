package com.closedcircuit.closedcircuitapplication.common.domain.model

import com.closedcircuit.closedcircuitapplication.common.presentation.util.formatNumberToCurrency
import kotlinx.serialization.Serializable

@Serializable
data class Amount(val value: Double, val currency: Currency? = null) : Comparable<Amount> {

    init {
        require(value >= 0.toDouble()) { "Price ($value) cannot be less than 0.0" }
    }

    fun getFormattedValue(): String {
        return formatNumberToCurrency(value, currency?.value.orEmpty())
    }

    fun intValue(): Int = value.toInt()

    operator fun minus(other: Amount): Amount {
        return Amount(value - other.value, currency)
    }

    operator fun div(other: Amount): Amount {
        return if (other.value <= 0.0) Amount(0.0, currency)
        else Amount(value / other.value, currency)
    }

    operator fun plus(other: Amount): Amount {
        return Amount(value + other.value, currency)
    }

    override fun compareTo(other: Amount): Int {
        return compareValuesBy(this, other, Amount::value)
    }
}