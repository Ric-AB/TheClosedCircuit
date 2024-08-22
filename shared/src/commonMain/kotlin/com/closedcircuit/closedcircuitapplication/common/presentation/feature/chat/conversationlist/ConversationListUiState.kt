package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationlist

import com.closedcircuit.closedcircuitapplication.common.domain.chat.Conversation
import kotlinx.collections.immutable.ImmutableList

sealed interface ConversationListUiState {
    object Loading : ConversationListUiState

    data class Content(val conversations: ImmutableList<Conversation>) : ConversationListUiState

    data class Error(val message: String) : ConversationListUiState
}