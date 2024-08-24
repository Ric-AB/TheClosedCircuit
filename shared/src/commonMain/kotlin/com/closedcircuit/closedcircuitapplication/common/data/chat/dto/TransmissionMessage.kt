package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransmissionMessage(
    val message: String,
    val type: String,
    @SerialName("receiver_id")
    val receiverId: String,
    @SerialName("sender_id")
    val senderId: String
)
