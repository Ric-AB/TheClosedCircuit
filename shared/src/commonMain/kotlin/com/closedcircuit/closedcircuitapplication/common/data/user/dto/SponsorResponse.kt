package com.closedcircuit.closedcircuitapplication.common.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorResponse(
    @SerialName("sponsor_avatar")
    val sponsorAvatar: String,
    @SerialName("sponsor_fullname")
    val sponsorFullName: String,
    @SerialName("loan_amount")
    val loanAmount: Double
)
