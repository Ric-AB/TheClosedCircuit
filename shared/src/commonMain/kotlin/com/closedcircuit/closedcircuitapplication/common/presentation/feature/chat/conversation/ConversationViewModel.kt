package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val otherParticipantID: ID,
    private val chatRepository: ChatRepository,
    userRepository: UserRepository
) : ScreenModel {
    private val user = userRepository.userFlow
    val state = mutableStateOf(produceState())

    init {
        screenModelScope.launch {
            chatRepository.initSession(state.value.currentUserId)
                .onSuccess { println("#####SUCCESS") }
                .onError { _, message -> println("#####ERROR:: $message") }
        }
    }

    fun onEvent(event: ConversationUiEvent) {
        when (event) {
            is ConversationUiEvent.MessageChange -> updateNewMessage(event.message)
            ConversationUiEvent.SendMessage -> sendMessage()
        }
    }

    private fun sendMessage() {

    }

    private fun produceState(): ConversationUiState {
        return ConversationUiState(
            loading = false,
            currentUserId = user.value!!.id,
            newMessageField = InputField(),
            messages = persistentListOf()
        )
    }

    private fun updateNewMessage(message: String) {
        state.value.newMessageField.onValueChange(message)
    }
}