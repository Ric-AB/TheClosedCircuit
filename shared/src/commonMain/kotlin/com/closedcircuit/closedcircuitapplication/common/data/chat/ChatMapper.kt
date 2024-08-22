package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiChatUser
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiConversation
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiMessage
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Conversation
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
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
    registrationToken = registrationToken
)

fun ApiConversation.toConversation(): Conversation {
    val chatUser = ChatUser(
        ID.generateRandomUUID(), avatar = null,
        fullName = Name(value = "eum"),
        profile = ProfileType.SPONSOR,
        registrationToken = null
    )

    return Conversation(
        id = ID(id),
        name = name,
        chatUser = chatUser,
        participants = participants.map { ID(it) },
        lastMessage = messages.last().toMessage(),
        createdAt = Date(createdAt),
        updateAt = Date(updatedAt)
    )
}

fun ApiMessage.toMessage() = Message(
    id = ID(id),
    senderID = ID(sender),
    content = content,
    createAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun List<ApiConversation>.toConversations() = map { it.toConversation() }

fun List<ApiMessage>.toMessages() = map { it.toMessage() }

fun List<ApiChatUser>.toChatUsers() = map { it.toChatUser(ProfileType.SPONSOR) }