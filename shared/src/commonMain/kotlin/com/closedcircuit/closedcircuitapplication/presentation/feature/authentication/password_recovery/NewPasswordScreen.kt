@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.ContentWithMessageBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.LoadingDialog
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.PasswordOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object NewPasswordScreen : Screen, KoinComponent {
    private val resetPasswordKoinContainer: ResetPasswordKoinContainer by inject()
    private val viewModel: ResetPasswordViewModel = resetPasswordKoinContainer.scope.get()


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.state
        val onEvent = viewModel::onEvent
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(state.resetPasswordResult) {
            when (state.resetPasswordResult) {
                is ResetPasswordResult.Failure -> {
                    messageBarState.addError(state.resetPasswordResult.message)
                    onEvent(ResetPasswordUIEvent.ResultHandled)
                }

                ResetPasswordResult.Success -> {
                    delay(500)
                    messageBarState.addSuccess("Password reset successfully") {
                        onEvent(ResetPasswordUIEvent.ResultHandled)
                        navigator.replaceAll(LoginScreen)
                    }
                }

                null -> Unit
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
        isLoading = state.loading
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            TitleText(stringResource(SharedRes.strings.reset_your_password))

            Spacer(modifier = Modifier.height(8.dp))
            BodyText(stringResource(SharedRes.strings.finally_were_here))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))
                PasswordOutlinedTextField(
                    value = state.password,
                    onValueChange = { onEvent(ResetPasswordUIEvent.PasswordChange(it)) },
                    label = stringResource(SharedRes.strings.new_password),
                    placeholder = { Text(stringResource(SharedRes.strings.enter_a_new_password)) },
                    onPasswordVisibilityChange = {},
                    isError = false,
                    showPassword = false,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = {})
                )

                Spacer(modifier = Modifier.height(8.dp))
                PasswordOutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { onEvent(ResetPasswordUIEvent.ConfirmPasswordChange(it)) },
                    label = stringResource(SharedRes.strings.confirm_password),
                    placeholder = { Text(stringResource(SharedRes.strings.confirm_password)) },
                    onPasswordVisibilityChange = {},
                    isError = false,
                    showPassword = false,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = {})
                )

                Spacer(modifier = Modifier.height(40.dp))
                DefaultButton(onClick = { onEvent(ResetPasswordUIEvent.SubmitPassword) }) {
                    Text(stringResource(SharedRes.strings.reset_password))
                }
            }

            if (state.loading) {
                LoadingDialog()
            }
        }
    }
}