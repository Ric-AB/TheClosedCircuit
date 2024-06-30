package com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetApprovalRequest(
    val status: String
)
