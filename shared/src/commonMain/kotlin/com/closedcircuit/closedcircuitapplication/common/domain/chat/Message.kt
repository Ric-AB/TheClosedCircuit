package com.closedcircuit.closedcircuitapplication.common.domain.chat

import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: ID,
    val senderID: ID,
    val conversationName: String,
    val content: String,
    val contentType: MessageType,
    val isMine: Boolean,
    val createAt: Date,
    val updatedAt: Date
)
