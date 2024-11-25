package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneratePaymentLinkRequest(
    @SerialName("loan_id")
    val loanId: String,
    val category: String,
    val type: String,
    val amount: Int
)
