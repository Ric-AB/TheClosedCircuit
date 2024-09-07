package com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.ApiBudget
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto.ApiStep
import com.closedcircuit.closedcircuitapplication.sponsor.data.fundrequest.SponsorApiFundRequest
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
    val duration: Int,
    @SerialName("estimated_selling_price")
    val estimatedSellingPrice: String,
    @SerialName("estimated_cost_price")
    val estimatedCostPrice: String,
    @SerialName("target_amount")
    val targetAmount: String,
    @SerialName("funds_raised")
    val fundsRaised: Double?,
    @SerialName("tasks_completed")
    val tasksCompleted: Double? = null,
    @SerialName("total_funds_raised")
    val totalFundsRaised: String? = null,
    @SerialName("plan_analytics")
    val analytics: String,
    val wallet: String,
    val user: String,
    val currency: String,
    @SerialName("has_requested_fund")
    val hasRequestedFund: Boolean?,
    @SerialName("is_sponsored")
    val isSponsored: Boolean?,
    @SerialName("fund_request")
    val fundRequest: SponsorApiFundRequest,
    @SerialName("accountability_partners")
    val accountabilityPartners: List<String>,
    @SerialName("beneficiary_fullname")
    val beneficiaryFullName: String?,
    @SerialName("beneficiary_id")
    val beneficiaryId: String? = null,
    val steps: List<ApiStep>,
    val budgets: List<ApiBudget>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
