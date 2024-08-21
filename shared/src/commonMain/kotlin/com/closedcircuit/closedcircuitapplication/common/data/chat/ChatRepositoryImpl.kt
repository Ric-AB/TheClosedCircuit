package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess

class ChatRepositoryImpl(
    private val chatService: ChatService
) : ChatRepository {

    override suspend fun getConversationPartners(activeProfileType: ProfileType): ApiResponse<List<ChatUser>> {
        return chatService.getConversationPartners(activeProfileType.value).mapOnSuccess {
            it.chatUsers.toChatUsers()
        }
    }
}