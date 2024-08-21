package com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationpartners

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation.ConversationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class ConversationPartnersScreen(private val activeProfile: ProfileType) : Screen,
    KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel =
            getScreenModel<ConversationPartnersViewModel> { parametersOf(activeProfile) }

        ScreenContent(
            state = viewModel.state.value,
            goBack = navigator::pop,
            navigateToConversationScreen = { navigator.replace(ConversationScreen(it)) }
        )
    }

    @Composable
    private fun ScreenContent(
        state: ConversationPartnersUiState,
        goBack: () -> Unit,
        navigateToConversationScreen: (ChatUser) -> Unit
    ) {
        BaseScaffold(
            topBar = { DefaultAppBar(title = getHeader(), mainAction = goBack) }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (state) {
                    is ConversationPartnersUiState.Content -> Content(
                        state = state,
                        navigateToConversationScreen = navigateToConversationScreen
                    )

                    is ConversationPartnersUiState.Error -> {}
                    ConversationPartnersUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Content(
        state: ConversationPartnersUiState.Content,
        navigateToConversationScreen: (ChatUser) -> Unit
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = verticalScreenPadding)
        ) {
            items(items = state.partners, key = { it.id.value }) { chatUser ->
                ChatUserItem(
                    chatUser = chatUser,
                    modifier = Modifier.fillMaxWidth()
                        .clickable { navigateToConversationScreen(chatUser) }
                )
            }
        }
    }

    @Composable
    private fun ChatUserItem(modifier: Modifier, chatUser: ChatUser) {
        Row(
            modifier = modifier.padding(vertical = 12.dp, horizontal = horizontalScreenPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(imageUrl = chatUser.avatar?.value.orEmpty(), size = DpSize(40.dp, 40.dp))

            Spacer(Modifier.width(12.dp))
            Text(
                text = chatUser.fullName.value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }

    @Composable
    private fun getHeader(): String {
        return remember(activeProfile) {
            when (activeProfile) {
                ProfileType.BENEFICIARY -> ProfileType.SPONSOR.getPlural()
                ProfileType.SPONSOR -> ProfileType.BENEFICIARY.getPlural()
            }
        }
    }
}