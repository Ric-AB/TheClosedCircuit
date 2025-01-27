package com.closedcircuit.closedcircuitapplication.common.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val email: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val name: String? = null,
    val roles: String? = null
)