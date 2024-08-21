package com.closedcircuit.closedcircuitapplication.common.data.chat.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetConversationsDto(
    val conversations: List<ApiConversation>
)
