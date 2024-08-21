package com.closedcircuit.closedcircuitapplication.common.data.chat

import com.closedcircuit.closedcircuitapplication.common.data.chat.dto.GetConversationPartnersDto
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_CONVERSATION_PARTNERS
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface ChatService {

    @GET(GET_CONVERSATION_PARTNERS)
    suspend fun getConversationPartners(@Query("view") partnerType: String): ApiResponse<GetConversationPartnersDto>
}