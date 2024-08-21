package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.ApiConversation
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.GetConversationPartnersDto
import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.GetConversationsDto
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_CONVERSATIONS
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_CONVERSATION_MESSAGES
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_CONVERSATION_PARTNERS
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface ChatService {

    @GET(GET_CONVERSATION_PARTNERS)
    suspend fun getConversationPartners(@Query("view") partnerType: String): ApiResponse<GetConversationPartnersDto>

    @GET(GET_CONVERSATIONS)
    suspend fun getConversations(): ApiResponse<GetConversationsDto>

    @GET(GET_CONVERSATION_MESSAGES)
    suspend fun getMessagesForConversation(
        @Query("name") conversationName: String,
        @Path("id") userId: String
    ): ApiResponse<ApiConversation>
}