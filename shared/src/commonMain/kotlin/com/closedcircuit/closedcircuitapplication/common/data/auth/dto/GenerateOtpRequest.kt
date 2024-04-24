package com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenerateOtpRequest(
    val email: String
)
