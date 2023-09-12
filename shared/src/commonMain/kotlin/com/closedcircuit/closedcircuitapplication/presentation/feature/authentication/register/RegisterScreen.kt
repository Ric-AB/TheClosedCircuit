package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.PasswordOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard.DashboardTab
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.screenContentPadding
import com.closedcircuit.closedcircuitapplication.presentation.util.observerWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object RegisterScreen : Screen, KoinComponent {
    private val viewModel: RegisterViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.state
        val messageBarState = rememberMessageBarState()


        viewModel.resultResultChannel.receiveAsFlow().observerWithScreen {
            when (it) {
                is RegisterResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                RegisterResult.Success -> {
                    delay(500) //wait for loader to hide
                    navigator.replaceAll(DashboardTab)
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            state = state,
            onEvent = viewModel::onEvent,
            navigateToLogin = { navigator.replaceAll(LoginScreen) }
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    state: RegisterUIState,
    onEvent: (RegisterUIEvent) -> Unit,
    navigateToLogin: () -> Unit
) {
    BaseScaffold(
        topBar = { DefaultAppBar(mainAction = navigateToLogin) },
        messageBarState = messageBarState,
        isLoading = state.isLoading,
        contentWindowInsets = WindowInsets.ime
    ) { innerPadding ->

        var showPassword by rememberSaveable { mutableStateOf(false) }
        var showConfirmPassword by rememberSaveable { mutableStateOf(false) }
        val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberField, passwordField, confirmPasswordField, _) = state
        val inputFieldCommonModifier = Modifier.fillMaxWidth()

        LazyColumn(
            contentPadding = PaddingValues(bottom = screenContentPadding.calculateBottomPadding()),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            item {
                BodyText(
                    text = stringResource(SharedRes.strings.shared_build_shared_prosperity),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))
                TitleText(text = stringResource(SharedRes.strings.create_a_new_account))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    DefaultOutlinedTextField(
                        value = firstNameField.value,
                        onValueChange = { onEvent(RegisterUIEvent.FirstNameChange(it)) },
                        label = stringResource(SharedRes.strings.first_name),
                        isError = firstNameField.isError,
                        supportingText = {
                            if (firstNameField.isError) {
                                Text(text = firstNameField.error)
                            } else {
                                NamePrompt()
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        modifier = inputFieldCommonModifier.onFocusChanged {
                            if (it.isFocused) {
                                onEvent(RegisterUIEvent.InputFieldFocusReceived(firstNameField.name))
                            } else {
                                onEvent(RegisterUIEvent.InputFieldFocusLost)
                            }
                        }
                    )

                    DefaultOutlinedTextField(
                        value = nickNameField.value,
                        onValueChange = { onEvent(RegisterUIEvent.NickNameChange(it)) },
                        label = stringResource(SharedRes.strings.preferred) + "/" +
                                stringResource(SharedRes.strings.nick_name),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                    )

                    DefaultOutlinedTextField(
                        value = lastNameField.value,
                        onValueChange = { onEvent(RegisterUIEvent.LastNameChange(it)) },
                        label = stringResource(SharedRes.strings.last_name),
                        isError = lastNameField.isError,
                        supportingText = {
                            if (lastNameField.isError) {
                                Text(text = lastNameField.error)
                            } else {
                                NamePrompt()
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        modifier = inputFieldCommonModifier.onFocusChanged {
                            if (it.isFocused) {
                                onEvent(RegisterUIEvent.InputFieldFocusReceived(lastNameField.name))
                            } else {
                                onEvent(RegisterUIEvent.InputFieldFocusLost)
                            }
                        }
                    )

                    DefaultOutlinedTextField(
                        value = emailField.value,
                        onValueChange = { onEvent(RegisterUIEvent.EmailChange(it)) },
                        label = stringResource(SharedRes.strings.email),
                        isError = emailField.isError,
                        supportingText = { Text(text = emailField.error) },
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = inputFieldCommonModifier.onFocusChanged {
                            if (it.isFocused) {
                                onEvent(RegisterUIEvent.InputFieldFocusReceived(emailField.name))
                            } else {
                                onEvent(RegisterUIEvent.InputFieldFocusLost)
                            }
                        }
                    )

                    DefaultOutlinedTextField(
                        value = phoneNumberField.value,
                        onValueChange = { onEvent(RegisterUIEvent.PhoneNumberChange(it)) },
                        label = stringResource(SharedRes.strings.phone_number),
                        isError = phoneNumberField.isError,
                        supportingText = { Text(text = phoneNumberField.error) },
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        modifier = inputFieldCommonModifier.onFocusChanged {
                            if (it.isFocused) {
                                onEvent(RegisterUIEvent.InputFieldFocusReceived(phoneNumberField.name))
                            } else {
                                onEvent(RegisterUIEvent.InputFieldFocusLost)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    PasswordOutlinedTextField(
                        value = passwordField.value,
                        onValueChange = { onEvent(RegisterUIEvent.PasswordChange(it)) },
                        label = stringResource(SharedRes.strings.password),
                        onPasswordVisibilityChange = { showPassword = it },
                        showPassword = showPassword,
                        showCriteria = true,
                        errors = passwordField.error.split(Regex("\n")),
                        imeAction = ImeAction.Next,
                    )

                    PasswordOutlinedTextField(
                        value = confirmPasswordField.value,
                        onValueChange = { onEvent(RegisterUIEvent.ConfirmPasswordChange(it)) },
                        label = stringResource(SharedRes.strings.confirm_password),
                        onPasswordVisibilityChange = { showConfirmPassword = it },
                        showPassword = showConfirmPassword,
                        isError = confirmPasswordField.isError,
                        supportingText = {
                            if (confirmPasswordField.isError) {
                                Text(confirmPasswordField.error)
                            }
                        },
                        imeAction = ImeAction.Done,
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    DefaultButton(onClick = { onEvent(RegisterUIEvent.Submit) }) {
                        Text(text = stringResource(SharedRes.strings.create_account))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        BodyText(text = stringResource(SharedRes.strings.already_have_an_account))

                        Spacer(modifier = Modifier.width(4.dp))
                        BodyText(
                            text = stringResource(SharedRes.strings.login),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.clickable(onClick = navigateToLogin)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NamePrompt() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(SharedRes.strings.name_check_prompt),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}