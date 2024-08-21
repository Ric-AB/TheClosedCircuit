package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
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
                    message = state.newMessage,
                    onMessageChange = { onEvent(ConversationUiEvent.MessageChange(it)) },
                    onSendMessage = { onEvent(ConversationUiEvent.SendMessage) }
                )
            }
        }
    }

    @Composable
    private fun NewMessage(
        message: String,
        onMessageChange: (String) -> Unit,
        onSendMessage: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            MessageTextField(
                value = message,
                onValueChange = onMessageChange,
                maxLines = 6,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = MaterialTheme.shapes.extraLarge
            )

            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                onClick = onSendMessage,
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
    }

    @Composable
    private fun MessageTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        enabled: Boolean = true,
        singleLine: Boolean = true,
        maxLines: Int,
        shape: Shape = TextFieldDefaults.shape,
        trailingIcon: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        placeHolder: @Composable (() -> Unit)? = null,
    ) {
        val interactionSource = remember {
            MutableInteractionSource()
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation,
            maxLines = maxLines,
            enabled = enabled,
            singleLine = singleLine,
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                trailingIcon = trailingIcon,
                placeholder = placeHolder,
                leadingIcon = leadingIcon,
                shape = shape,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
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
                modifier = Modifier
            )
        }
    }

    @Composable
    private fun ParticipantMessage(message: String) {
        ChatMessage(
            message = message,
            backgroundColor = Color.White,
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceTint,
                shape = RoundedCornerShape(4.dp, 50.dp, 50.dp, 15.dp)
            )
        )
    }

    @Composable
    private fun ChatMessage(modifier: Modifier, message: String, backgroundColor: Color) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .sizeIn(minWidth = 80.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(4.dp, 50.dp, 50.dp, 15.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp).then(modifier)
        )
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
                    val textColor = Color.White
                    Text(
                        text = otherParticipantName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                    Text(text = otherParticipantProfile, color = textColor)
                }
            }
        )
    }
}