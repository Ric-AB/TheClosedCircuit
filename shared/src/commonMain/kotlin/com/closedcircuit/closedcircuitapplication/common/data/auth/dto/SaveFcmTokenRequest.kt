package com.closedcircuit.closedcircuitapplication.common.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SaveFcmTokenRequest(
    @SerialName("registration_token")
    val registrationToken: String,
    @SerialName("user")
    val userId: String,
    @SerialName("timestamp")
    val timestamp: String
)
