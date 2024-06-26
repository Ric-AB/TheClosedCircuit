package com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val email: String,
    val name: String,
    val roles: String,
    @SerialName("phone_number")
    val phoneNumber: String
)