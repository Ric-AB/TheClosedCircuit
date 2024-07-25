package com.closedcircuit.closedcircuitapplication.common.data.fundrequest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiFundRequest(
    val id: String?,
    @SerialName("plan")
    val planId: String? = null,
    @SerialName("beneficiary")
    val beneficiaryId: String? = null,
    @SerialName("means_of_support")
    val meansOfSupport: String?,
    @SerialName("minimum_loan_range")
    val minimumLoanRange: String?,
    @SerialName("maximum_loan_range")
    val maximumLoanRange: String?,
    @SerialName("maximum_number_of_lenders")
    val maxLenders: Int?,
    val currency: String?,
    @SerialName("grace_duration")
    val graceDuration: Int?,
    @SerialName("repayment_duration")
    val repaymentDuration: Int?,
    @SerialName("interest_rate")
    val interestRate: Int?,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)
