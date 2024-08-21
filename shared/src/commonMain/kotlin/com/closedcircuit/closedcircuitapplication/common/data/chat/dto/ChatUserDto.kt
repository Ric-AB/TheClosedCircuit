package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatUserDto(
    val id: String,
    val avatar: String?,
    @SerialName("fullname")
    val fullName: String,
    @SerialName("registration_token")
    val registrationToken: String?
)
