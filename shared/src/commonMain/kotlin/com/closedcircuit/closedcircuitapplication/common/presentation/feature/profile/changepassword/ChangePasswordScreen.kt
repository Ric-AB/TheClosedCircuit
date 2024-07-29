package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.changepassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PasswordOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class ChangePasswordScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val messageBarState = rememberMessageBarState()
        val viewModel = getScreenModel<ChangePasswordViewModel>()

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is ChangePasswordResult.ChangePasswordError -> {
                    messageBarState.addError(it.message)
                }

                ChangePasswordResult.ChangePasswordSuccess ->
                    messageBarState.addSuccess("Password updated successfully.") {
                        navigator.pop()
                    }
            }
        }

        ScreenContent(
            state = viewModel.state,
            messageBarState = messageBarState,
            onEvent = viewModel::onEvent,
            goBack = navigator::pop
        )
    }

    @Composable
    private fun ScreenContent(
        state: ChangePasswordUiState,
        messageBarState: MessageBarState,
        onEvent: (ChangePasswordUiEvent) -> Unit,
        goBack: () -> Unit
    ) {
        BaseScaffold(
            topBar = { DefaultAppBar(mainAction = goBack) },
            messageBarState = messageBarState,
            showLoadingDialog = state.isLoading
        ) { innerPadding ->
            var showOldPassword by rememberSaveable { mutableStateOf(false) }
            var showNewPassword by rememberSaveable { mutableStateOf(false) }
            var showConfirmPassword by rememberSaveable { mutableStateOf(false) }
            val (_, oldPasswordField, newPasswordField, confirmPasswordField) = state

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = horizontalScreenPadding,
                        vertical = verticalScreenPadding
                    ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TitleText(stringResource(SharedRes.strings.change_password_label))
                PasswordOutlinedTextField(
                    inputField = oldPasswordField,
                    onValueChange = { onEvent(ChangePasswordUiEvent.OldPasswordChange(it)) },
                    label = stringResource(SharedRes.strings.old_password_label),
                    onPasswordVisibilityChange = { showOldPassword = it },
                    showPassword = showOldPassword,
                    imeAction = ImeAction.Next,
                )

                PasswordOutlinedTextField(
                    inputField = newPasswordField,
                    onValueChange = { onEvent(ChangePasswordUiEvent.NewPasswordChange(it)) },
                    label = stringResource(SharedRes.strings.new_password),
                    onPasswordVisibilityChange = { showNewPassword = it },
                    showPassword = showNewPassword,
                    showCriteria = true,
                    errors = newPasswordField.error.split(Regex("\n")),
                    imeAction = ImeAction.Next,
                )

                PasswordOutlinedTextField(
                    inputField = confirmPasswordField,
                    onValueChange = { onEvent(ChangePasswordUiEvent.ConfirmPasswordChange(it)) },
                    label = stringResource(SharedRes.strings.confirm_password),
                    onPasswordVisibilityChange = { showConfirmPassword = it },
                    showPassword = showConfirmPassword,
                    isError = confirmPasswordField.isError,
                    supportingText = { Text(confirmPasswordField.error) },
                    imeAction = ImeAction.Done,
                )

                Spacer(Modifier.height(20.dp))
                DefaultButton(
                    onClick = { onEvent(ChangePasswordUiEvent.Submit) },
                    enabled = state.canSubmit
                ) {
                    Text(text = stringResource(SharedRes.strings.change_password_label))
                }
            }
        }
    }
}