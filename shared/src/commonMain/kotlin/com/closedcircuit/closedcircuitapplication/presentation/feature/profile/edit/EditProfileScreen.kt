package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.SlideUpTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal data class EditProfileScreen(private val user: User) :
    Screen,
    KoinComponent,
    CustomScreenTransition by SlideUpTransition {
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
    onEvent: (EditProfileUiEvent) -> Unit
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
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->

        uiState?.let { uiState ->
            val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberField) = uiState
            val inputFieldCommonModifier = Modifier.fillMaxWidth()
            val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
                if (isFocused) onEvent(EditProfileUiEvent.InputFieldFocusReceived(fieldName))
                else onEvent(EditProfileUiEvent.InputFieldFocusLost)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = verticalScreenPadding)
            ) {
                DefaultOutlinedTextField(
                    inputField = firstNameField,
                    onValueChange = { onEvent(EditProfileUiEvent.FirstNameChange(it)) },
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
                    onValueChange = { onEvent(EditProfileUiEvent.NickNameChange(it)) },
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
                    inputField = lastNameField,
                    onValueChange = { onEvent(EditProfileUiEvent.LastNameChange(it)) },
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
                    onValueChange = { onEvent(EditProfileUiEvent.EmailChange(it)) },
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

                DefaultOutlinedTextField(
                    inputField = phoneNumberField,
                    onValueChange = { onEvent(EditProfileUiEvent.PhoneNumberChange(it)) },
                    label = stringResource(SharedRes.strings.phone_number),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    modifier = inputFieldCommonModifier.onFocusChanged {
                        handleFocusChange(it.isFocused, phoneNumberField.name)
                    }
                )

                DefaultButton(onClick = { onEvent(EditProfileUiEvent.OnSubmit) }) {
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