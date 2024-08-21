package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.runtime.Immutable
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ConversationUiState(
    val loading: Boolean = false,
    val currentUserId: ID,
    val newMessageField: InputField,
    val messages: ImmutableList<Message> = persistentListOf()
)
