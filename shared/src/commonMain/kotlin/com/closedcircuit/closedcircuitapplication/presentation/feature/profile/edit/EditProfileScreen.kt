package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.ModalTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal data class EditProfileScreen(private val user: User) :
    Screen,
    KoinComponent,
    CustomScreenTransition by ModalTransition {
    private val viewModel: EditProfileViewModel by inject { parametersOf(user) }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = viewModel.state
        val messageBarState = rememberMessageBarState()

        viewModel.editProfileResult.receiveAsFlow().observerWithScreen {
            when (it) {
                is EditProfileResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                EditProfileResult.Success -> {
                    messageBarState.addSuccess("Changes made successfully") {
                        navigator.pop()
                    }
                }
            }
        }

        ScreenContent(
            goBack = navigator::pop,
            messageBarState = messageBarState,
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ScreenContent(
    goBack: () -> Unit,
    messageBarState: MessageBarState,
    uiState: EditProfileUIState?,
    onEvent: (EditProfileUIEvent) -> Unit
) {
    BaseScaffold(
        messageBarState = messageBarState,
        isLoading = uiState?.isLoading ?: false,
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.edit_profile),
                mainIcon = Icons.Rounded.Close,
                mainAction = goBack
            )
        }
    ) { innerPadding ->

        uiState?.let { uiState ->
            val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberField) = uiState
            val inputFieldCommonModifier = Modifier.fillMaxWidth()

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = defaultHorizontalScreenPadding)
            ) {
                DefaultOutlinedTextField(
                    value = firstNameField.value,
                    onValueChange = { onEvent(EditProfileUIEvent.FirstNameChange(it)) },
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
                            onEvent(EditProfileUIEvent.InputFieldFocusReceived(firstNameField.name))
                        } else {
                            onEvent(EditProfileUIEvent.InputFieldFocusLost)
                        }
                    }
                )

                DefaultOutlinedTextField(
                    value = nickNameField.value,
                    onValueChange = { onEvent(EditProfileUIEvent.NickNameChange(it)) },
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
                    onValueChange = { onEvent(EditProfileUIEvent.LastNameChange(it)) },
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
                            onEvent(EditProfileUIEvent.InputFieldFocusReceived(lastNameField.name))
                        } else {
                            onEvent(EditProfileUIEvent.InputFieldFocusLost)
                        }
                    }
                )

                DefaultOutlinedTextField(
                    value = emailField.value,
                    onValueChange = { onEvent(EditProfileUIEvent.EmailChange(it)) },
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
                            onEvent(EditProfileUIEvent.InputFieldFocusReceived(emailField.name))
                        } else {
                            onEvent(EditProfileUIEvent.InputFieldFocusLost)
                        }
                    }
                )

                DefaultOutlinedTextField(
                    value = phoneNumberField.value,
                    onValueChange = { onEvent(EditProfileUIEvent.PhoneNumberChange(it)) },
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
                            onEvent(EditProfileUIEvent.InputFieldFocusReceived(phoneNumberField.name))
                        } else {
                            onEvent(EditProfileUIEvent.InputFieldFocusLost)
                        }
                    }
                )

                DefaultButton(onClick = { onEvent(EditProfileUIEvent.OnSubmit) }) {
                    Text(text = stringResource(SharedRes.strings.save_changes))
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