package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class)
internal class ConversationScreen(private val otherParticipant: ChatUser) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel =
            getScreenModel<ConversationViewModel> { parametersOf(otherParticipant.id) }

        ScreenContent(
            state = viewModel.state.value,
            onEvent = viewModel::onEvent,
            goBack = navigator::pop
        )
    }

    @Composable
    private fun ScreenContent(
        state: ConversationUiState,
        onEvent: (ConversationUiEvent) -> Unit,
        goBack: () -> Unit,
    ) {
        BaseScaffold(
            topBar = {
                ConversationAppBar(
                    otherParticipantName = otherParticipant.fullName.value,
                    otherParticipantProfile = otherParticipant.profile.displayText,
                    goBack = goBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    reverseLayout = true,
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    items(state.messages) {
                        if (it.senderID.value == state.currentUserId.value) {
                            ParticipantMessage(message = it.content)
                        } else {
                            MyMessage(message = it.content)
                        }
                    }
                }

                NewMessage(
                    messageField = state.newMessageField,
                    onMessageChange = { onEvent(ConversationUiEvent.MessageChange(it)) },
                    onSendMessage = { onEvent(ConversationUiEvent.SendMessage) }
                )
            }
        }
    }

    @Composable
    private fun NewMessage(
        messageField: InputField,
        onMessageChange: (String) -> Unit,
        onSendMessage: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            MessageTextField(
                inputField = messageField,
                onValueChange = onMessageChange,
                maxLines = 6,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                trailingIcon = {
                    FilledIconButton(
                        onClick = onSendMessage,
                        enabled = messageField.value.isNotBlank(),
                        modifier = Modifier.fillMaxHeight()
                            .aspectRatio(1F)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "send message",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    }

    @Composable
    private fun MyMessage(message: String) {
        Row {
            Spacer(Modifier.weight(1f))
            ChatMessage(
                message = message,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
            )
        }
    }

    @Composable
    private fun ParticipantMessage(message: String) {
        ChatMessage(
            message = message,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
        )
    }

    @Composable
    private fun ChatMessage(
        modifier: Modifier,
        message: String,
        backgroundColor: Color,
        contentColor: Color
    ) {
        Surface(
            color = backgroundColor,
            contentColor = contentColor,
            shape = RoundedCornerShape(4.dp, 50.dp, 50.dp, 15.dp),
            modifier = Modifier
                .sizeIn(minWidth = 80.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        }
    }


    @Composable
    private fun ConversationAppBar(
        otherParticipantName: String,
        otherParticipantProfile: String,
        goBack: () -> Unit
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            title = {
                Column {
                    Text(
                        text = otherParticipantName,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = otherParticipantProfile,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }
}