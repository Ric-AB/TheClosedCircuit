package com.closedcircuit.closedcircuitapplication.common.domain.chat

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun initSession(userID: ID): ApiResponse<Unit>

    suspend fun sendMessage(message: String)

    suspend fun getMessagesForConversationAsFlow(): Flow<Message>

    suspend fun getConversationPartners(activeProfileType: ProfileType): ApiResponse<List<ChatUser>>

    suspend fun getConversations(): ApiResponse<List<Conversation>>

    suspend fun getMessagesForConversation(
        userID: ID,
        conversationName: String
    ): ApiResponse<List<Message>>

    suspend fun closeSession()
}