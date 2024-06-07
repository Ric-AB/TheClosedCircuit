package com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanOfferDto(
    @SerialName("loan_offer_id")
    val loanOfferId: String?,
    @SerialName("business_name")
    val businessName: String?,
    @SerialName("beneficiary_name")
    val beneficiaryName: String?,
    @SerialName("beneficiary_avatar")
    val beneficiaryAvatar: String?,
    @SerialName("plan_sector")
    val planSector: String?,
    @SerialName("loan_amount")
    val loanAmount: String?,
    @SerialName("created_at")
    val createdAt: String?
)
