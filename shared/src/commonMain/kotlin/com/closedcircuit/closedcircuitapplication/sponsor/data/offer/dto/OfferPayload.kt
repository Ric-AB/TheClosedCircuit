package com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferPayload(
    @SerialName("other_amount")
    val otherAmount: String?,
    @SerialName("funding_level")
    val fundingLevel: String,
    @SerialName("fund_request")
    val fundRequest: String,
    @SerialName("is_donation")
    val isDonation: Boolean?,
    val steps: List<String>?,
    val budgets: List<String>?
)