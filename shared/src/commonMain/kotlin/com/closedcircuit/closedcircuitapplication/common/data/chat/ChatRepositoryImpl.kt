package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiMessage
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.TransmissionMessage
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Conversation
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.Constants
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(
    private val userRepository: UserRepository,
    private val chatService: ChatService,
    private val webSocketHttpClient: HttpClient
) : ChatRepository {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(userID: ID): ApiResponse<Unit> {
        return try {
            val id = userID.value
            socket = webSocketHttpClient.webSocketSession {
                url("${Constants.CHAT_SOCKET_BASE_URL}ws/chat/?user_id=$id")
            }

            if (socket?.isActive == true) ApiSuccessResponse(Unit)
            else ApiErrorResponse(
                errorMessage = "Couldn't establish connection",
                httpStatusCode = -1
            )
        } catch (e: Exception) {
            ApiResponse.create(Throwable(e.message ?: "Something went wrong"))
        }
    }

    override suspend fun sendMessage(message: String, receiverID: ID, senderID: ID) {
        try {
            val request = TransmissionMessage(
                message = message,
                receiverId = receiverID.value,
                senderId = senderID.value,
                type = "text"
            )

            socket?.send(Frame.Text(Json.encodeToString(request)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getMessagesForConversationAsFlow(): Flow<Message> {
        return try {
            socket?.incoming?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as Frame.Text).readText()
                    val message = Json.decodeFromString<ApiMessage>(json)
                    val currentUserId = userRepository.getCurrentUser().id
                    message.toMessage(currentUserId)
                } ?: flowOf()
        } catch (e: Exception) {
            flowOf()
        }
    }

    override suspend fun getConversationPartners(
        activeProfileType: ProfileType,
        currentUserId: ID
    ): ApiResponse<List<ChatUser>> {
        return chatService.getConversationPartners(activeProfileType.value).mapOnSuccess {
            it.chatUsers.filterNot { chatUser ->
                chatUser.id == currentUserId.value
            }.toChatUsers()
        }
    }

    override suspend fun getConversations(): ApiResponse<List<Conversation>> {
        val currentUserId = userRepository.getCurrentUser().id
        return chatService.getConversations().mapOnSuccess {
            it.conversations.toConversations(currentUserId)
        }
    }

    override suspend fun getMessagesForConversation(
        userID: ID,
        conversationName: String
    ): ApiResponse<List<Message>> {
        return chatService.getMessagesForConversation(
            userId = userID.value,
            conversationName = conversationName
        ).mapOnSuccess { it.messages.reversed().toMessages(userID) }
    }

    override fun closeSession() {
        CoroutineScope(NonCancellable).launch {
            socket?.close()
            socket = null
        }
    }
}