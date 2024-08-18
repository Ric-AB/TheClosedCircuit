package com.closedcircuit.closedcircuitapplication.common.presentation.feature.message.conversationpartners

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel

class ConversationPartnersViewModel : ScreenModel {

    val state = mutableStateOf(ConversationPartnersUiState.Loading)
}