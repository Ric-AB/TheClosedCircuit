package com.closedcircuit.closedcircuitapplication.common.presentation.feature.message.conversationpartners

sealed interface ConversationPartnersUiState {

    object Loading : ConversationPartnersUiState

    data class Content(val partners: List<String>) : ConversationPartnersUiState

    data class Error(val message: String) : ConversationPartnersUiState
}