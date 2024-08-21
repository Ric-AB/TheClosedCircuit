package com.closedcircuit.closedcircuitapplication.common.domain.chat

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: ID,
    val name: String,
    val participants: List<ID>,
    val lastMessage: Message
)
