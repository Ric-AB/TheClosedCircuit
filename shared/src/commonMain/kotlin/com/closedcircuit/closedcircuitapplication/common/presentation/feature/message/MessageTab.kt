package com.closedcircuit.closedcircuitapplication.common.presentation.feature.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.message.conversationpartners.ConversationPartnersScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ScreenKey
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

internal object MessageTab : Tab {
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

        val messageBarState = rememberMessageBarState()

        ScreenContent(
            messageBarState = messageBarState,
            navigateToConversationPartners = { navigator.push(ConversationPartnersScreen()) }
        )
    }

    @Composable
    private fun ScreenContent(
        messageBarState: MessageBarState,
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