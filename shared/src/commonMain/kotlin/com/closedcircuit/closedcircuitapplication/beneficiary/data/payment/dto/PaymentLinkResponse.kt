package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentLinkResponse(
    val link: String
)
