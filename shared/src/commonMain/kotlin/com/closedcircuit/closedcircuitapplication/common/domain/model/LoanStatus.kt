package com.closedcircuit.closedcircuitapplication.beneficiary.domain.model

enum class LoanStatus {
    PENDING, ACCEPTED, PAID, DECLINED;

    companion object {
        val map = values().associateBy { it.name.lowercase() }

        fun fromText(text: String) = map[text]
    }
}