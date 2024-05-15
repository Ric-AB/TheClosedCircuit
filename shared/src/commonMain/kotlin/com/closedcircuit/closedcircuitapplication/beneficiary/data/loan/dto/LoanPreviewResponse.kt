package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanPreviewResponse(
    @SerialName("plans")
    val previews: List<ApiLoanPreview>
)
