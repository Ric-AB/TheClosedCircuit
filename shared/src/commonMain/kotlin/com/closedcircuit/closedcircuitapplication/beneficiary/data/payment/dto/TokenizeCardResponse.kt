package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenizeCardResponse(
    val link: String
)
