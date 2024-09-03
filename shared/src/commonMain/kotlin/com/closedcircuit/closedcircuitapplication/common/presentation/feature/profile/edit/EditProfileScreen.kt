package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.edit

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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PhoneOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideUpTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent

internal class EditProfileScreen :
    Screen,
    KoinComponent,
    CustomScreenTransition by SlideUpTransition() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<EditProfileViewModel>()
        val state = viewModel.state
        val messageBarState = rememberMessageBarState()

        viewModel.editProfileResult.receiveAsFlow().observeWithScreen {
            when (it) {
                is EditProfileResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                EditProfileResult.Success -> {
                    messageBarState.addSuccess("Profile updated successfully") {
                        navigator.pop()
                    }
                }
            }
        }

        ScreenContent(
            goBack = navigator::pop,
            messageBarState = messageBarState,
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ScreenContent(
    goBack: () -> Unit,
    messageBarState: MessageBarState,
    state: EditProfileUIState,
    onEvent: (EditProfileUiEvent) -> Unit
) {
    BaseScaffold(
        messageBarState = messageBarState,
        showLoadingDialog = state.isLoading,
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.edit_profile),
                mainIcon = Icons.Rounded.Close,
                mainAction = goBack
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->

        val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberState) = state
        val inputFieldCommonModifier = Modifier.fillMaxWidth()
        val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
            if (isFocused) onEvent(EditProfileUiEvent.InputFieldFocusReceived(fieldName))
            else onEvent(EditProfileUiEvent.InputFieldFocusLost)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalScreenPadding)
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

            PhoneOutlinedTextField(
                phoneNumberState = phoneNumberState,
                onValueChange = { onEvent(EditProfileUiEvent.PhoneStateChange(it)) },
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

            DefaultButton(onClick = { onEvent(EditProfileUiEvent.OnSubmit) }) {
                Text(text = stringResource(SharedRes.strings.save_changes))
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