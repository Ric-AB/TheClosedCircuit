package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLoanDetails(
    val id: String,
    @SerialName("interest_amount")
    val interestAmount: String?,
    @SerialName("loan_amount")
    val loanAmount: String?,
    @SerialName("repayment_amount")
    val repaymentAmount: String?,
    @SerialName("loan_schedule")
    val loanSchedule: List<ApiLoanSchedule>?,
    @SerialName("grace_period")
    val gracePeriod: Int?,
    val currency: String,
    @SerialName("sponsor_fullname")
    val sponsorFullName: String,
    @SerialName("avatar")
    val avatar: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("funding_level")
    val fundingLevel: String?,
    @SerialName("other_amount")
    val otherAmount: String?,
    @SerialName("is_donation")
    val isDonation: Boolean?,
    @SerialName("status")
    val status: String?,
    @SerialName("fund_request")
    val fundRequest: String?
)
