package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val otherParticipantID: ID,
    private val chatRepository: ChatRepository,
    userRepository: UserRepository
) : ScreenModel {
    private val userID = userRepository.userFlow.value?.id!!
    private val conversationName = createConversationName()
    val state = mutableStateOf(produceState())

    init {
        initSession()
        getMessages()
    }

    fun onEvent(event: ConversationUiEvent) {
        when (event) {
            is ConversationUiEvent.MessageChange -> updateNewMessage(event.message)
            ConversationUiEvent.SendMessage -> sendNewMessage()
        }
    }

    private fun initSession() {
        screenModelScope.launch {
            chatRepository.initSession(state.value.currentUserId)
                .onSuccess { listenToMessageEvents() }
        }
    }

    private fun getMessages() {
        screenModelScope.launch {
            chatRepository.getMessagesForConversation(
                userID = userID,
                conversationName = conversationName
            ).onSuccess {
                state.value = state.value.copy(messages = it.toImmutableList())
            }
        }
    }

    private fun listenToMessageEvents() {
//        chatRepository.getMessagesForConversationAsFlow()
//            .debounce(1.seconds)
//            .onEach {
//                println("MESSAGE RECEIVED $it")
//                getMessages()
//            }
//            .launchIn(screenModelScope)
    }

    private fun sendNewMessage() {
        val stateValue = state.value
        val message = stateValue.newMessageField.value
        val senderId = stateValue.currentUserId
        val receiverId = otherParticipantID

        screenModelScope.launch {
            chatRepository.sendMessage(
                message = message,
                receiverID = receiverId,
                senderID = senderId
            )

            stateValue.newMessageField.clear()
        }
    }

    private fun produceState(): ConversationUiState {
        return ConversationUiState(
            loading = false,
            currentUserId = userID,
            newMessageField = InputField(),
            messages = persistentListOf()
        )
    }

    private fun updateNewMessage(message: String) {
        state.value.newMessageField.onValueChange(message)
    }

    private fun createConversationName(): String {
        val minId = minOf(userID.value, otherParticipantID.value)
        val maxId = maxOf(userID.value, otherParticipantID.value)
        return "conv_${minId}_${maxId}"
    }

    override fun onDispose() {
        chatRepository.closeSession()
        super.onDispose()
    }
}