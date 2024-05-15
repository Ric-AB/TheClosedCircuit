package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLoanSchedule(
    val date: String,
    @SerialName("repayment_amount")
    val repaymentAmount: String,
    val status: String
)
