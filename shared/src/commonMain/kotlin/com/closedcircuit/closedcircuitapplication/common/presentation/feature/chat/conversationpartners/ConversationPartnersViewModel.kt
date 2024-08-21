package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationpartners

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class ConversationPartnersViewModel(
    private val activeProfile: ProfileType,
    private val chatRepository: ChatRepository,
) : ScreenModel {

    val state = mutableStateOf<ConversationPartnersUiState>(ConversationPartnersUiState.Loading)

    init {
        getConversationPartners()
    }

    private fun getConversationPartners() {
        screenModelScope.launch {
            chatRepository.getConversationPartners(activeProfile).onSuccess {
                state.value = ConversationPartnersUiState.Content(
                    partners = it.toImmutableList()
                )
            }.onError { _, message ->
                state.value = ConversationPartnersUiState.Error(message)
            }
        }
    }
}