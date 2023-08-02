package com.closedcircuit.closedcircuitapplication.data.user.model

data class LoginResponse(
    val token: String,
    val user_id: String,
    val firebase_custom_token: String,
    val fcm_server_key: String,
    val last_login: String
)
