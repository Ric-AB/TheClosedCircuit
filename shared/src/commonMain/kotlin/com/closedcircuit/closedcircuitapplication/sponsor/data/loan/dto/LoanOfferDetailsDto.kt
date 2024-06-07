package com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanOfferDetailsDto(
    val id: String,
    @SerialName("interest_amount")
    val interestAmount: String,
    @SerialName("loan_amount")
    val loanAmount: String,
    @SerialName("repayment_amount")
    val repaymentAmount: String,
    @SerialName("grace_period")
    val gracePeriod: String,
    @SerialName("sponsor_fullname")
    val sponsorFullName: String,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("interest_rate")
    val interestRate: String,
    @SerialName("repayment_duration")
    val repaymentDuration: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("funding_level")
    val fundingLevel: String,
    @SerialName("other_amount")
    val otherAmount: String?,
    @SerialName("status")
    val status: String,
    @SerialName("fund_request")
    val fundRequest: String,
    @SerialName("sponsor")
    val sponsor: String,
    @SerialName("steps")
    val steps: List<String>,
    @SerialName("budgets")
    val budgets: List<String>,
    @SerialName("is_step")
    val isStep: Boolean,
    @SerialName("is_budget")
    val isBudget: Boolean,
    @SerialName("is_other_amount")
    val isOtherAmount: Boolean,
)
