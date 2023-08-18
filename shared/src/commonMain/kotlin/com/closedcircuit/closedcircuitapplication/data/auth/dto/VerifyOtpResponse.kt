package com.closedcircuit.closedcircuitapplication.data.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponse(
    val message: String
)
