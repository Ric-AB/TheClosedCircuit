package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLoanPreview(
    @SerialName("business_name")
    val businessName: String,
    @SerialName("total_sponsors")
    val totalSponsors: Int,
    @SerialName("total_amount_offered")
    val totalAmountOffered: String,
    @SerialName("plan_id")
    val planId: String,
    @SerialName("sponsor_avatars")
    val sponsorAvatars: List<String>
)
