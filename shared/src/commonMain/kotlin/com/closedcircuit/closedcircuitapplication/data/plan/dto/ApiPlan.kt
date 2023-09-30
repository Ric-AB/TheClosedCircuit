package com.closedcircuit.closedcircuitapplication.data.plan.dto

import com.closedcircuit.closedcircuitapplication.data.budget.dto.ApiBudget
import com.closedcircuit.closedcircuitapplication.data.step.dto.ApiStep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiPlan(
    val id: String,
    val avatar: String,
    @SerialName("plan_category")
    val category: String,
    @SerialName("plan_sector")
    val sector: String,
    @SerialName("plan_type")
    val type: String,
    @SerialName("business_name")
    val name: String,
    @SerialName("plan_description")
    val description: String,
    @SerialName("plan_duration")
    val duration: String,
    @SerialName("estimated_selling_price")
    val estimatedSellingPrice: String,
    @SerialName("estimated_cost_price")
    val estimatedCostPrice: String,
    @SerialName("target_amount")
    val targetAmount: String,
    @SerialName("funds_raised")
    val fundsRaised: String,
    @SerialName("tasks_completed")
    val tasksCompleted: Double,
    @SerialName("total_funds_raised")
    val totalFundsRaised: String?,
    @SerialName("plan_analytics")
    val analytics: String,
    val wallet: String,
    val user: String,
    @SerialName("has_requested_fund")
    val hasRequestedFund: Boolean?,
    @SerialName("is_sponsored")
    val isSponsored: Boolean?,
    @SerialName("accountability_partners")
    val accountabilityPartners: List<String>,
    val steps: List<ApiStep>,
    val budgets: List<ApiBudget>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
