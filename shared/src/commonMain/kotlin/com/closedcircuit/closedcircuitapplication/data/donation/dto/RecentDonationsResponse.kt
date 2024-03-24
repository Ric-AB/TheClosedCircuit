package com.closedcircuit.closedcircuitapplication.data.donation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecentDonationsResponse(
    val donations: List<ApiDonation>
)
