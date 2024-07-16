package com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.ApiBudget
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiStep(
    @SerialName("plan")
    val planID: String,
    @SerialName("user")
    val userID: String,
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
    val budgets: List<ApiBudget>,
    @SerialName("is_sponsored")
    val isSponsored: Boolean,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
