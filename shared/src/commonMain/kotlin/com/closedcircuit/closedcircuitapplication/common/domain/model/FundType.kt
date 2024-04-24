package com.closedcircuit.closedcircuitapplication.beneficiary.domain.model

enum class FundType(val displayText: String, val requestValue: String) {
    LOAN("Loans", "loans"),
    DONATION("Donations (Cash gifts)", "donations"),
    BOTH("Both (Loans and Donations)", "both");

    companion object {
        val map = values().associateBy { it.requestValue }
        fun fromText(text: String?) =
            map[text] ?: throw RuntimeException("No matching value for $text")
    }
}