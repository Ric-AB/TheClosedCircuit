package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetConversationPartnersDto(
    @SerialName("users")
    val chatUsers: List<ChatUserDto>
)
