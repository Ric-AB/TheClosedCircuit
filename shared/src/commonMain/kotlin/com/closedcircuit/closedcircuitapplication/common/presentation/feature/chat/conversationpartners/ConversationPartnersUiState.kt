package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationpartners

import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import kotlinx.collections.immutable.ImmutableList

sealed interface ConversationPartnersUiState {

    object Loading : ConversationPartnersUiState

    data class Content(
        val partners: ImmutableList<ChatUser>
    ) : ConversationPartnersUiState

    data class Error(val message: String) : ConversationPartnersUiState
}