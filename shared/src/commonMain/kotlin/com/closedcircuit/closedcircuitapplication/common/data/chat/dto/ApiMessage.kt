package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    val id: String,
    @SerialName("sender")
    val senderId: String,
    @SerialName("conversation_name")
    val conversationName: String,
    val content: String,
    @SerialName("content_type")
    val contentType: String,
    val receiver: ApiChatUser,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
