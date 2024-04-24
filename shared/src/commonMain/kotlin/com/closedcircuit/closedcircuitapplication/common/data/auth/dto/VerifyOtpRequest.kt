package com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("otp_code")
    val otpCode: String,
    val email: String
)
