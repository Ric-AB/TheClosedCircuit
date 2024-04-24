package com.closedcircuit.closedcircuitapplication.beneficiary.data.donation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecentDonationsResponse(
    val donations: List<ApiDonation>
)
