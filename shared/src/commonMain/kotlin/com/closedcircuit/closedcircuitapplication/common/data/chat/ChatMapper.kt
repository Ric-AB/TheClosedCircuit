package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ChatUserDto
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType

fun ChatUserDto.toChatUser(profileType: ProfileType) = ChatUser(
    id = ID(id),
    avatar = avatar?.let { ImageUrl(it) },
    fullName = Name(fullName),
    profile = profileType,
    registrationToken = registrationToken
)

fun List<ChatUserDto>.toChatUsers() = map { it.toChatUser(ProfileType.SPONSOR) }