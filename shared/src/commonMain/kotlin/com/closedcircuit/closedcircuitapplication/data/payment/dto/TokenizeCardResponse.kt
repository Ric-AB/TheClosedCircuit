package com.closedcircuit.closedcircuitapplication.data.payment.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenizeCardResponse(
    val link: String
)
