package com.closedcircuit.closedcircuitapplication.common.domain.model

enum class LoanStatus {
    PENDING, ACCEPTED, PAID, DECLINED, CANCELLED;

    fun value() = this.name.lowercase()

    companion object {
        val map = values().associateBy { it.name.lowercase() }

        fun fromText(text: String) = map[text]
    }
}