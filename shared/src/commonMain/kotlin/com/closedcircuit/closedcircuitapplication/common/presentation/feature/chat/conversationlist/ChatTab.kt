package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.chat.Conversation
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation.ConversationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationpartners.ConversationPartnersScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ScreenKey
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

internal object ChatTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(SharedRes.strings.message)
            val icon = painterResource(SharedRes.images.ic_double_chat_bubble)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator =
            findNavigator(ScreenKey.PROTECTED_NAVIGATOR_SCREEN, LocalNavigator.currentOrThrow)

        val rootViewModel = findRootNavigator(navigator).getNavigatorScreenModel<RootViewModel>()
        val viewModel = navigator.getNavigatorScreenModel<ConversationListViewModel>()
        val messageBarState = rememberMessageBarState()
        val activeProfile = rootViewModel.state.collectAsState().value?.activeProfile

        ScreenContent(
            messageBarState = messageBarState,
            state = viewModel.state.value,
            navigateToConversationScreen = { navigator.push(ConversationScreen(it)) },
            navigateToConversationPartners = {
                navigator.push(
                    ConversationPartnersScreen(
                        activeProfile!!
                    )
                )
            }
        )
    }

    @Composable
    private fun ScreenContent(
        messageBarState: MessageBarState,
        state: ConversationListUiState,
        navigateToConversationScreen: (ChatUser) -> Unit,
        navigateToConversationPartners: () -> Unit
    ) {
        BaseScaffold(
            messageBarState = messageBarState,
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.message),
                    mainIcon = null
                )
            },
            floatingActionButton = { NewConversationFab(onClick = navigateToConversationPartners) }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                when (state) {
                    is ConversationListUiState.Content -> {
                        Content(
                            state = state,
                            navigateToConversationScreen = navigateToConversationScreen
                        )
                    }

                    is ConversationListUiState.Error -> {}
                    ConversationListUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Content(
        state: ConversationListUiState.Content,
        navigateToConversationScreen: (ChatUser) -> Unit
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = verticalScreenPadding)
        ) {
            items(items = state.conversations, key = { it.id.value }) { conversation ->
                val chatUser = conversation.chatUser
                ConversationItem(
                    conversation = conversation,
                    chatUser = chatUser,
                    modifier = Modifier.fillMaxWidth()
                        .clickable { navigateToConversationScreen(chatUser) }
                )
            }
        }
    }

    @Composable
    private fun ConversationItem(
        modifier: Modifier,
        chatUser: ChatUser,
        conversation: Conversation
    ) {
        Row(
            modifier = modifier.padding(vertical = 12.dp, horizontal = horizontalScreenPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(imageUrl = chatUser.avatar?.value.orEmpty(), size = DpSize(40.dp, 40.dp))

            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = chatUser.fullName.value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(Modifier.height(4.dp))
                Text(conversation.lastMessage.content)
            }
        }
    }

    @Composable
    private fun NewConversationFab(onClick: () -> Unit) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            text = { Text(text = stringResource(SharedRes.strings.start_chat_label)) },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
        )
    }
}