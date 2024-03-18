package com.closedcircuit.closedcircuitapplication.data.loan.dto

import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus
import kotlinx.serialization.Serializable

@Serializable
data class AcknowledgeLoanPayload(
    val status: String
)
