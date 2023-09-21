
package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


internal object ResetPasswordEmailScreen : Screen, KoinComponent {
    private val resetPasswordKoinContainer: ResetPasswordKoinContainer by inject()
    private val viewModel: ResetPasswordViewModel = resetPasswordKoinContainer.scope.get()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.state
        val onEvent = viewModel::onEvent
        val messageBarState = rememberMessageBarState()

        viewModel.requestOtpResult.receiveAsFlow().observerWithScreen {
            when (it) {
                is RequestOtpResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                is RequestOtpResult.Success -> {
                    delay(300)
                    navigator.push(ResetPasswordOtpScreen)
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            state = state,
            onEvent = onEvent,
            goBack = navigator::pop
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    state: ResetPasswordUIState,
    onEvent: (ResetPasswordUIEvent) -> Unit,
    goBack: () -> Unit
) {
    BaseScaffold(
        topBar = { DefaultAppBar(mainAction = goBack) },
        messageBarState = messageBarState,
        isLoading = state.loading,
        contentWindowInsets = WindowInsets.ime
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            TitleText(text = stringResource(SharedRes.strings.password_recovery))

            Spacer(modifier = Modifier.height(8.dp))
            BodyText(text = stringResource(SharedRes.strings.we_are_here_to_help_you_recover_password))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(SharedRes.images.recover_password_illurstration),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))
                DefaultOutlinedTextField(
                    value = state.emailField.value,
                    onValueChange = { onEvent(ResetPasswordUIEvent.EmailChange(it)) },
                    label = stringResource(SharedRes.strings.email),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                DefaultButton(onClick = { onEvent(ResetPasswordUIEvent.RequestOtp()) }) {
                    Text(stringResource(SharedRes.strings.reset_password))
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    BodyText(text = stringResource(SharedRes.strings.remember_password))

                    Spacer(modifier = Modifier.width(4.dp))
                    BodyText(
                        text = stringResource(SharedRes.strings.login),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable(onClick = goBack)
                    )
                }
            }
        }
    }
}