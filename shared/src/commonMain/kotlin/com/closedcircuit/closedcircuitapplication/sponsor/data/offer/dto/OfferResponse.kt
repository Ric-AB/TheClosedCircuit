package com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto

import kotlinx.serialization.Serializable

@Serializable
data class OfferResponse(
    val id: String,
    val paymentLink: String
)