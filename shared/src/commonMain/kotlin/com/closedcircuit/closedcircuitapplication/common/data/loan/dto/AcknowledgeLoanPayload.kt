package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus
import kotlinx.serialization.Serializable

@Serializable
data class AcknowledgeLoanPayload(
    val status: String
)
