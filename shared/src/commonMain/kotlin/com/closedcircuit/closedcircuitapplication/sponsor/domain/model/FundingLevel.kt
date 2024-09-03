package com.closedcircuit.closedcircuitapplication.sponsor.domain.model

enum class FundingLevel(val label: String, val requestValue: String) {
    PLAN(label = "Plan", requestValue = "plan"),
    STEP(label = "Steps", requestValue = "step"),
    BUDGET(label = "Budgets", requestValue = "budget"),
    OTHER(label = "Other", requestValue = "other_amount");

    companion object {
        val map = values().associateBy { it.requestValue }
        fun fromText(text: String): FundingLevel =
            map[text] ?: throw RuntimeException("No matching value for $text")
    }
}