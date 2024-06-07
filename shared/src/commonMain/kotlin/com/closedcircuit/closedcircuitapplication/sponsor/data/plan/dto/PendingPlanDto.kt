package com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PendingPlanDto(
    val id: String,
    val avatar: String,
    @SerialName("business_name")
    val planName: String,
    @SerialName("plan_category")
    val planCategory: String,
    @SerialName("plan_sector")
    val planSector: String,
    @SerialName("plan_type")
    val planType: String,
    val currency: String?,
    @SerialName("plan_description")
    val planDescription: String,
    @SerialName("plan_duration")
    val planDuration: String,
    @SerialName("estimated_selling_price")
    val estimatedSellingPrice: String,
    @SerialName("beneficiary_id")
    val beneficiaryId: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)
