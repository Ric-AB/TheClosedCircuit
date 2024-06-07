package com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetLoanOffersDto(
    val loans: List<LoanOfferDto>
)
