package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PasswordOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PhoneOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ProtectedNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.delayReplaceAll
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.MakeOfferNavigator
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent

internal class RegisterScreen(private val planID: ID? = null) : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<RegisterViewModel>()
        val state = viewModel.state
        val messageBarState = rememberMessageBarState()


        viewModel.registerResultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is RegisterResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                is RegisterResult.Success -> {
                    if (planID != null) {
                        navigator.delayReplaceAll(MakeOfferNavigator(planID))
                    } else {
                        navigator.delayReplaceAll(ProtectedNavigator(it.activeProfile))
                    }
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            state = state.value,
            onEvent = viewModel::onEvent,
            navigateToLogin = { navigator.pop() }
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    state: RegisterUIState,
    onEvent: (RegisterUiEvent) -> Unit,
    navigateToLogin: () -> Unit
) {
    BaseScaffold(
        topBar = { DefaultAppBar(mainAction = navigateToLogin) },
        messageBarState = messageBarState,
        showLoadingDialog = state.isLoading,
    ) { innerPadding ->

        var showPassword by rememberSaveable { mutableStateOf(false) }
        var showConfirmPassword by rememberSaveable { mutableStateOf(false) }
        val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberState, passwordField, confirmPasswordField, _) = state
        val inputFieldCommonModifier = Modifier.fillMaxWidth()
        val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
            if (isFocused) onEvent(RegisterUiEvent.InputFieldFocusReceived(fieldName))
            else onEvent(RegisterUiEvent.InputFieldFocusLost)
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalScreenPadding)
                .padding(bottom = verticalScreenPadding)
                .imePadding()
        ) {
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
                    inputField = firstNameField,
                    onValueChange = { onEvent(RegisterUiEvent.FirstNameChange(it)) },
                    label = stringResource(SharedRes.strings.first_name),
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
                        handleFocusChange(it.isFocused, firstNameField.name)
                    }
                )

                DefaultOutlinedTextField(
                    inputField = nickNameField,
                    onValueChange = { onEvent(RegisterUiEvent.NickNameChange(it)) },
                    label = stringResource(SharedRes.strings.preferred) + "/" +
                            stringResource(SharedRes.strings.nick_name) + "(optional)",
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    ),
                )

                DefaultOutlinedTextField(
                    inputField = lastNameField,
                    onValueChange = { onEvent(RegisterUiEvent.LastNameChange(it)) },
                    label = stringResource(SharedRes.strings.last_name),
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
                        handleFocusChange(it.isFocused, lastNameField.name)
                    }
                )

                DefaultOutlinedTextField(
                    inputField = emailField,
                    onValueChange = { onEvent(RegisterUiEvent.EmailChange(it)) },
                    label = stringResource(SharedRes.strings.email),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = inputFieldCommonModifier.onFocusChanged {
                        handleFocusChange(it.isFocused, emailField.name)
                    }
                )

                PhoneOutlinedTextField(
                    phoneNumberState = phoneNumberState,
                    onValueChange = { onEvent(RegisterUiEvent.PhoneStateChange(it)) },
                    onCountrySelect = { onEvent(RegisterUiEvent.PhoneStateChange(it)) },
                    label = stringResource(SharedRes.strings.phone_number),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    modifier = inputFieldCommonModifier.onFocusChanged {
                        handleFocusChange(it.isFocused, phoneNumberState.inputField.name)
                    }
                )

                PasswordOutlinedTextField(
                    inputField = passwordField,
                    onValueChange = { onEvent(RegisterUiEvent.PasswordChange(it)) },
                    label = stringResource(SharedRes.strings.password),
                    onPasswordVisibilityChange = { showPassword = it },
                    showPassword = showPassword,
                    showCriteria = true,
                    errors = passwordField.error.split(Regex("\n")),
                    imeAction = ImeAction.Next,
                )

                PasswordOutlinedTextField(
                    inputField = confirmPasswordField,
                    onValueChange = { onEvent(RegisterUiEvent.ConfirmPasswordChange(it)) },
                    label = stringResource(SharedRes.strings.confirm_password),
                    onPasswordVisibilityChange = { showConfirmPassword = it },
                    showPassword = showConfirmPassword,
                    isError = confirmPasswordField.isError,
                    supportingText = { Text(confirmPasswordField.error) },
                    imeAction = ImeAction.Done,
                )

                Spacer(modifier = Modifier.height(40.dp))
                DefaultButton(onClick = { onEvent(RegisterUiEvent.Submit) }) {
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
