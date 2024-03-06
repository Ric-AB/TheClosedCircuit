package com.closedcircuit.closedcircuitapplication.data.fundrequest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiFundRequest(
    val id: String?,
    @SerialName("plan")
    val planId: String?,
    @SerialName("beneficiary")
    val beneficiaryId: String?,
    @SerialName("means_of_support")
    val meansOfSupport: String?,
    @SerialName("minimum_loan_range")
    val minimumLoanRange: String?,
    @SerialName("maximum_loan_range")
    val maximumLoanRange: String?,
    @SerialName("maximum_number_of_lenders")
    val maxLenders: Int?,
    @SerialName("grace_duration")
    val graceDuration: Int?,
    @SerialName("repayment_duration")
    val repaymentDuration: Int?,
    @SerialName("interest_rate")
    val interestRate: Int?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("updated_at")
    val updatedAt: String?
)
