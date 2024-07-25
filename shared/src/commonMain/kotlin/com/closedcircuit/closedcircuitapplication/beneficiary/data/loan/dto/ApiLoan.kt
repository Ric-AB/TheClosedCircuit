package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLoan(
    val avatar: String,
    @SerialName("sponsor_fullname")
    val sponsorFullName: String,
    @SerialName("loan_amount")
    val loanAmount: String,
    val currency: String,
    @SerialName("grace_period")
    val gracePeriod: Int,
    @SerialName("interest_rate")
    val interestRate: Int,
    @SerialName("loan_offer_id")
    val loanOfferId: String,
    @SerialName("created_at")
    val createdAt: String
)
