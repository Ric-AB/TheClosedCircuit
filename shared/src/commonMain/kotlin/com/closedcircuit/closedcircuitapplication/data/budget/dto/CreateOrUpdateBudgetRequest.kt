package com.closedcircuit.closedcircuitapplication.data.budget.dto

import kotlinx.serialization.SerialName

data class CreateOrUpdateBudgetRequest(
    @SerialName("budget_name")
    val name: String,
    @SerialName("budget_description")
    val description: String,
    @SerialName("budget_cost")
    val cost: Double,
    @SerialName("plan")
    val planID: String,
    @SerialName("step")
    val stepID: String = ""
)
