package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiMessage
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Conversation
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiSuccessResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(
    private val chatService: ChatService,
    private val webSocketHttpClient: HttpClient
) : ChatRepository {

    private val socket: WebSocketSession? = null
    override suspend fun initSession(userID: ID, otherParticipantID: ID): ApiResponse<Unit> {
        return try {
            val ids = listOf(userID.value, otherParticipantID.value).joinToString(",")
            webSocketHttpClient.webSocketSession {
                url("wss://theclosedcircuit-staging.herokuapp.com/ws/chat/?participants=$ids")
            }

            if (socket != null) {
                ApiSuccessResponse(Unit)
            } else {
                ApiErrorResponse("Couldn't establish connection", -1)
            }
        } catch (e: Exception) {
            ApiResponse.create(Throwable(e.message ?: "Something went wrong"))
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getMessagesForConversationAsFlow(): Flow<Message> {
        return try {
            socket?.incoming?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as Frame.Text).readText()
                    val message = Json.decodeFromString<ApiMessage>(json)
                    message.toMessage()
                } ?: flowOf()
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf()
        }
    }

    override suspend fun getConversationPartners(activeProfileType: ProfileType): ApiResponse<List<ChatUser>> {
        return chatService.getConversationPartners(activeProfileType.value).mapOnSuccess {
            it.chatUsers.toChatUsers()
        }
    }

    override suspend fun getConversations(): ApiResponse<List<Conversation>> {
        return chatService.getConversations().mapOnSuccess {
            it.conversations.toConversations()
        }
    }

    override suspend fun getMessagesForConversation(
        userID: ID,
        conversationName: String
    ): ApiResponse<List<Message>> {
        return chatService.getMessagesForConversation(
            userId = userID.value,
            conversationName = conversationName
        ).mapOnSuccess { it.messages.toMessages() }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}