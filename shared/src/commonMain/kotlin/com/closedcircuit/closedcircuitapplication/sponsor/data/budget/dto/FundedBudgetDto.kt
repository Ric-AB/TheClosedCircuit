package com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto

import kotlinx.serialization.SerialName

data class FundedBudgetDto(
    val id: String,
    @SerialName("plan")
    val planID: String,
    @SerialName("step")
    val stepID: String,
    @SerialName("user")
    val userID: String,
    @SerialName("budget_name")
    val name: String,
    @SerialName("budget_description")
    val description: String,
    @SerialName("budget_cost")
    val cost: String,
    @SerialName("is_sponsored")
    val isSponsored: Boolean,
    @SerialName("funds_raised")
    val fundsRaised: Float,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("approvers")
    val approverIds: List<String>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)