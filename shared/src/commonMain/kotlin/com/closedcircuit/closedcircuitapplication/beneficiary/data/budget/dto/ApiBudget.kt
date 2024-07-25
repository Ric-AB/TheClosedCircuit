package com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto

import com.closedcircuit.closedcircuitapplication.common.data.model.FileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiBudget(
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
    val currency: String,
    @SerialName("is_sponsored")
    val isSponsored: Boolean,
    @SerialName("funds_raised")
    val fundsRaised: Float,
    @SerialName("is_completed")
    val isCompleted: Boolean?,
    val approvers: List<String>,
    val proof: List<FileDto>? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
