package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

enum class FundingLevel(val value: String) {
    PLAN(value = "plan"),
    STEP(value = "step"),
    BUDGET(value = "budget"),
    OTHER(value = "other");

    fun getLabel(): String = when (this) {
        PLAN -> "Plan"
        STEP -> "Steps"
        BUDGET -> "Budgets"
        OTHER -> "Other"
    }
}