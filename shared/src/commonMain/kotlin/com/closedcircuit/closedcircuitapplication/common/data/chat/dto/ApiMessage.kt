package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    val id: String,
    val sender: String,
    val content: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
