@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.PasswordOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.presentation.theme.calculateHorizontalPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.screenContentPadding
import com.closedcircuit.closedcircuitapplication.presentation.util.observerWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
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

        viewModel.resetPasswordResult.receiveAsFlow().observerWithScreen {
            when (it) {
                is ResetPasswordResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                ResetPasswordResult.Success -> {
                    delay(300)
                    navigator.replaceAll(LoginScreen)
                    viewModel.onDispose()
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
        isLoading = state.loading
    ) { innerPadding ->
        val (_, _, passwordField, confirmPasswordField, _) = state
        var showPassword by rememberSaveable { mutableStateOf(false) }
        var showConfirmPassword by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = screenContentPadding.calculateHorizontalPadding())
        ) {
            TitleText(stringResource(SharedRes.strings.reset_your_password))

            Spacer(modifier = Modifier.height(8.dp))
            BodyText(stringResource(SharedRes.strings.finally_were_here))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))
                PasswordOutlinedTextField(
                    value = passwordField.value,
                    onValueChange = { onEvent(ResetPasswordUIEvent.PasswordChange(it)) },
                    label = stringResource(SharedRes.strings.new_password),
                    placeholder = { Text(stringResource(SharedRes.strings.enter_a_new_password)) },
                    onPasswordVisibilityChange = { showPassword = !showConfirmPassword },
                    showPassword = showPassword,
                    showCriteria = true,
                    errors = passwordField.error.split(Regex("\n")),
                    imeAction = ImeAction.Next,
                )

                Spacer(modifier = Modifier.height(8.dp))
                PasswordOutlinedTextField(
                    value = confirmPasswordField.value,
                    onValueChange = { onEvent(ResetPasswordUIEvent.ConfirmPasswordChange(it)) },
                    label = stringResource(SharedRes.strings.confirm_password),
                    placeholder = { Text(stringResource(SharedRes.strings.confirm_password)) },
                    onPasswordVisibilityChange = { showConfirmPassword = !showConfirmPassword },
                    isError = confirmPasswordField.isError,
                    showPassword = showConfirmPassword,
                    supportingText = {
                        if (confirmPasswordField.isError) {
                            Text(text = confirmPasswordField.error)
                        }
                    },
                    imeAction = ImeAction.Done,
                )

                Spacer(modifier = Modifier.height(40.dp))
                DefaultButton(onClick = { onEvent(ResetPasswordUIEvent.SubmitPassword) }) {
                    Text(stringResource(SharedRes.strings.reset_password))
                }
            }
        }
    }
}