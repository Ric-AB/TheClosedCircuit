package com.closedcircuit.closedcircuitapplication.common.domain.chat

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface ChatRepository {

    suspend fun getConversationPartners(activeProfileType: ProfileType): ApiResponse<List<ChatUser>>
}