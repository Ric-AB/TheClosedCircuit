package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Message
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

@Immutable
data class ConversationUiState(
    val loading: Boolean,
    val currentUserId: ID,
    val newMessageField: InputField,
    val messages: SnapshotStateList<Message>
)

sealed interface ConversationResult {
    data class ConnectionError(val message: String) : ConversationResult
}
