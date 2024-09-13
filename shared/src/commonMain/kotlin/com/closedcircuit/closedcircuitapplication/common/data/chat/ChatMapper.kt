package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiChatUser
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiConversation
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiMessage
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Conversation
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
import com.closedcircuit.closedcircuitapplication.common.domain.chat.MessageType
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType

fun ApiChatUser.toChatUser(profileType: ProfileType) = ChatUser(
    id = ID(id),
    avatar = avatar?.let { ImageUrl(it) },
    fullName = Name(fullName),
    profile = profileType,
)

fun ApiConversation.toConversation(currentUserId: ID): Conversation {
    return Conversation(
        id = ID(id),
        name = name,
        participant = participant.toChatUser(ProfileType.SPONSOR),
        lastMessage = messages.lastOrNull()?.toMessage(currentUserId),
        createdAt = Date(createdAt),
        updateAt = Date(updatedAt)
    )
}

fun ApiMessage.toMessage(currentUserId: ID): Message {
    val senderID = ID(senderId)
    return Message(
        id = ID(id),
        senderID = senderID,
        conversationName = conversationName,
        content = content,
        contentType = MessageType.fromText(contentType),
        isMine = senderID == currentUserId,
        createAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

fun List<ApiConversation>.toConversations(currentUserId: ID) =
    map { it.toConversation(currentUserId) }

fun List<ApiMessage>.toMessages(currentUserID: ID) = map { it.toMessage(currentUserID) }

fun List<ApiChatUser>.toChatUsers() = map { it.toChatUser(ProfileType.SPONSOR) }