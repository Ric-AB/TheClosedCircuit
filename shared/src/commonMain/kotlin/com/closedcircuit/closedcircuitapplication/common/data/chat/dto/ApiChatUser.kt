package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiChatUser(
    val id: String,
    val avatar: String?,
    @SerialName("fullname")
    val fullName: String,
)
