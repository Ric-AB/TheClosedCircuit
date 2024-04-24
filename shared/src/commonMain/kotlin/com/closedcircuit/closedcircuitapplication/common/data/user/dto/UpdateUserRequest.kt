package com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    @SerialName("fullname")
    val fullName: String,
    val email: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val avatar: String
)
