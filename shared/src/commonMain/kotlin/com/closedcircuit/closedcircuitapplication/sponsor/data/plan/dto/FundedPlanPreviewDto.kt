package com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FundedPlanPreviewDto(
    @SerialName("plan_id")
    val id: String,
    @SerialName("plan_avatar")
    val avatar: String,
    @SerialName("plan_sector")
    val sector: String,
    @SerialName("beneficiary_fullname")
    val beneficiaryFullName: String,
    @SerialName("beneficiary_id")
    val beneficiaryId: String?,
    val currency: String?,
    @SerialName("amount_funded")
    val amountFunded: String,
    @SerialName("funding_type")
    val fundingType: String,
    @SerialName("funding_level")
    val fundingLevel: String,
    @SerialName("funding_date")
    val fundingDate: String,
    @SerialName("funds_raised")
    val fundsRaisedPercent: Double,
    @SerialName("tasks_completed")
    val tasksCompletedPercent: Double
)
