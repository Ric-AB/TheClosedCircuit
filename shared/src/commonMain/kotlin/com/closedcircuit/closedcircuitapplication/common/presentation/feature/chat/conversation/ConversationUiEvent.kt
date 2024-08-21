package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

sealed interface ConversationUiEvent {

    data class MessageChange(val message: String) : ConversationUiEvent
    object SendMessage : ConversationUiEvent
}