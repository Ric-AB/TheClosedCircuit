package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.runtime.Immutable
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ConversationUiState(
    val loading: Boolean = false,
    val currentUserId: ID,
    val newMessage: String,
    val messages: ImmutableList<Message> = persistentListOf()
)
