package com.closedcircuit.closedcircuitapplication.common.domain.chat

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.core.serialization.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class ChatUser(
    val id: ID,
    val avatar: ImageUrl?,
    val fullName: Name,
    val profile: ProfileType,
): JavaSerializable
