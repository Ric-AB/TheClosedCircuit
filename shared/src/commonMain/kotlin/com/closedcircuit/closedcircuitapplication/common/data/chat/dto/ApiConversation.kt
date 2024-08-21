package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiConversation(
    val id: String,
    val name: String,
    val participants: List<String>,
    val messages: List<ApiMessage>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
