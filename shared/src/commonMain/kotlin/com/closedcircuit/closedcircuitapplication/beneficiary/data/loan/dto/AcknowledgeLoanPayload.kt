package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import kotlinx.serialization.Serializable

@Serializable
data class AcknowledgeLoanPayload(
    val status: String
)
