package com.closedcircuit.closedcircuitapplication.data.loan.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoansResponse(
    val plans: List<ApiLoan>
)
