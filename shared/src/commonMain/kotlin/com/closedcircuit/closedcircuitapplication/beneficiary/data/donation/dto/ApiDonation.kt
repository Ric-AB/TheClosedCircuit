package com.closedcircuit.closedcircuitapplication.beneficiary.data.donation.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiDonation(
    @SerialName("donation_id")
    val donationId: String,
    @SerialName("sponsor_avatar")
    val sponsorAvatar: String,
    @SerialName("sponsor_fullname")
    val sponsorFullName: String,
    @SerialName("plan_name")
    val planName: String,
    val amount: String,
    val currency: String,
)
