package com.closedcircuit.closedcircuitapplication.common.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    @SerialName("fullname")
    val fullName: String,
    val roles: String?,
    @SerialName("phone_number")
    val phoneNumber: String,
    val password: String,
    @SerialName("confirm_password")
    val confirmPassword: String
)
