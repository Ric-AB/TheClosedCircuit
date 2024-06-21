package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

enum class FundingLevel(val value: String) {
    PLAN(value = "plan"),
    STEP(value = "step"),
    BUDGET(value = "budget"),
    OTHER(value = "other_amount");

    fun getLabel(): String = when (this) {
        PLAN -> "Plan"
        STEP -> "Steps"
        BUDGET -> "Budgets"
        OTHER -> "Other"
    }

    companion object {
        val map = values().associateBy { it.value }
        fun fromText(text: String): FundingLevel =
            map[text] ?: throw RuntimeException("No matching value for $text")
    }
}