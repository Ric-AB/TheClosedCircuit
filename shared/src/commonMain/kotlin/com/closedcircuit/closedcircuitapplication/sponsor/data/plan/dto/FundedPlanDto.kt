package com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto

import com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto.FundedStepDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FundedPlanDto(
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
    val duration: Int,
    @SerialName("estimated_selling_price")
    val estimatedSellingPrice: Double,
    @SerialName("estimated_cost_price")
    val estimatedCostPrice: Double,
    @SerialName("plan_analytics")
    val analytics: String?,
    @SerialName("target_amount")
    val targetAmount: Double,
    val wallet: String?,
    @SerialName("user")
    val userId: String,
    val currency: String,
    @SerialName("beneficiary_id")
    val beneficiaryId: String,
    @SerialName("has_requested_fund")
    val hasRequestedFund: Boolean?,
    @SerialName("fund_request")
    val fundRequestId: String,
    @SerialName("is_sponsored")
    val isSponsored: Boolean?,
    val steps: List<FundedStepDto>,
    @SerialName("accountability_partners")
    val accountabilityPartnerIds: List<String>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
