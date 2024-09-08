package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
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
        val scrollState = rememberLazyListState()
        val topBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
        val scope = rememberCoroutineScope()

        BaseScaffold(
            topBar = {
                ConversationAppBar(
                    otherParticipantAvatar = otherParticipant.avatar?.value.orEmpty(),
                    otherParticipantName = otherParticipant.fullName.value,
                    otherParticipantProfile = otherParticipant.profile.displayText,
                    goBack = goBack
                )
            },
            contentWindowInsets = ScaffoldDefaults
                .contentWindowInsets
                .exclude(WindowInsets.navigationBars)
                .exclude(WindowInsets.ime)
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    reverseLayout = true,
                    state = scrollState,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val messages = state.messages
                    itemsIndexed(messages) { index, message ->
                        val prevSenderId = messages.getOrNull(index - 1)?.senderID
                        val nextSenderId = messages.getOrNull(index + 1)?.senderID
                        val isPrevMessageByAuthor = prevSenderId == message.senderID
                        val isNextMessageByAuthor = nextSenderId == message.senderID

                        ChatMessage(
                            message = message.content,
                            isMine = message.isMine,
                            isPrevMessageByAuthor = isPrevMessageByAuthor,
                            isNextMessageByAuthor = isNextMessageByAuthor
                        )
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
        Surface(
            tonalElevation = 2.dp,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                MessageTextField(
                    inputField = messageField,
                    onValueChange = onMessageChange,
                    placeHolder = {
                        Text(
                            modifier = Modifier,
                            text = stringResource(SharedRes.strings.message_dots_label),
                        )
                    },
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = onSendMessage,
                            enabled = messageField.value.isNotBlank(),
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
    }

    @Composable
    private fun ChatMessage(
        message: String,
        isMine: Boolean,
        isPrevMessageByAuthor: Boolean,
        isNextMessageByAuthor: Boolean
    ) {
        val shape = getMessageBubbleShape(isMine, isPrevMessageByAuthor, isNextMessageByAuthor)
        val backgroundColor = if (isMine) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isMine) Spacer(Modifier.weight(1f))

                ChatMessageBubble(
                    message = message,
                    containerShape = shape,
                    backgroundColor = backgroundColor,
                )
            }

            MessageDivider(!isNextMessageByAuthor)
        }
    }

    @Composable
    private fun getMessageBubbleShape(
        isMine: Boolean,
        isPrevMessageByAuthor: Boolean,
        isNextMessageByAuthor: Boolean
    ): Shape {
        val roundShape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
        return remember(isMine, isPrevMessageByAuthor, isNextMessageByAuthor) {
            when {
                isPrevMessageByAuthor && isNextMessageByAuthor -> roundShape
                !isPrevMessageByAuthor -> RoundedCornerShape(
                    topStart = if (isMine) 50.dp else 15.dp,
                    topEnd = if (isMine) 15.dp else 50.dp,
                    bottomEnd = if (isMine) 4.dp else 50.dp,
                    bottomStart = if (isMine) 50.dp else 4.dp
                )

                else -> RoundedCornerShape(
                    topStart = if (isMine) 50.dp else 4.dp,
                    topEnd = if (isMine) 4.dp else 50.dp,
                    bottomEnd = if (isMine) 15.dp else 50.dp,
                    bottomStart = if (isMine) 50.dp else 15.dp
                )
            }
        }
    }

    @Composable
    private fun ChatMessageBubble(
        message: String,
        containerShape: Shape,
        backgroundColor: Color,
    ) {
        Surface(
            color = backgroundColor,
            shape = containerShape,
            modifier = Modifier.sizeIn(minWidth = 80.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }

    @Composable
    private fun MessageDivider(show: Boolean) {
        if (show) {
            Spacer(Modifier.height(8.dp))
        }
    }

    @Composable
    private fun ConversationAppBar(
        otherParticipantAvatar: String,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(imageUrl = otherParticipantAvatar, size = DpSize(30.dp, 30.dp))

                    Spacer(Modifier.width(8.dp))
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
            }
        )
    }
}