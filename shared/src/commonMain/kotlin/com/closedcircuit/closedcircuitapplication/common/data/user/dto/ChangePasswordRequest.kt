package com.closedcircuit.closedcircuitapplication.common.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @SerialName("confirm_password")
    val confirmPassword: String,
    @SerialName("old_password")
    val oldPassword: String,
    @SerialName("password")
    val newPassword: String
)
