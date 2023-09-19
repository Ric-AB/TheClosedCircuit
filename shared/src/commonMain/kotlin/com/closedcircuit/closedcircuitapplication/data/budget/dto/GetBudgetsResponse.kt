package com.closedcircuit.closedcircuitapplication.data.budget.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetBudgetsResponse(
    val budgets: List<ApiBudget>
)
