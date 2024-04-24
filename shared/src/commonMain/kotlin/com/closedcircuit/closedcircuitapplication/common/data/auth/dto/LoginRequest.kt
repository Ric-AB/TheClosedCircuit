package com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)