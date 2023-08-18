package com.closedcircuit.closedcircuitapplication.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponse(
    @SerialName("Success")
    val message: String
)
