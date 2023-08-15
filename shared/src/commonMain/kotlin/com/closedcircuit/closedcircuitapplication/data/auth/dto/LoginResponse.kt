package com.closedcircuit.closedcircuitapplication.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("firebase_custom_token")
    val firebaseCustomToken: String,
    @SerialName("fcm_server_key")
    val fcmServerKey: String,
    @SerialName("last_login")
    val lastLogin: String
)
