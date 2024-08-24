package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationlist

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class ConversationListViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ScreenModel {

    val state = mutableStateOf<ConversationListUiState>(ConversationListUiState.Loading)

    init {
//        initSession()
        getConversations()
    }

    private fun initSession() {
        screenModelScope.launch {
            val userId = userRepository.nonNullUser().id
            chatRepository.initSession(userID = userId)
        }
    }

    private fun getConversations() {
        screenModelScope.launch {
            chatRepository.getConversations().onSuccess {
                state.value = ConversationListUiState.Content(it.toImmutableList())
            }.onError { _, message ->
                state.value = ConversationListUiState.Error(message)
            }
        }
    }
}