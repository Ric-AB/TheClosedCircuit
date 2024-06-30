package com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto

import com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto.FundedBudgetDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FundedStepDto(
    val id: String,
    @SerialName("step_name")
    val name: String,
    @SerialName("step_description")
    val description: String,
    val duration: Int,
    @SerialName("target_funds")
    val targetFunds: String,
    @SerialName("total_funds_raised")
    val totalFundsRaised: String,
    @SerialName("plan")
    val planId: String,
    @SerialName("user")
    val userId: String,
    @SerialName("is_sponsored")
    val isSponsored: Boolean,
    val status: String,
    val budgets: List<FundedBudgetDto>,
    @SerialName("approvers")
    val approverIds: List<String>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)