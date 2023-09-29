package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.TextFieldTrailingText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

internal object SaveStepScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop, uiState = SaveStepUIState(), onEvent = {})
    }
}

@Composable
private fun ScreenContent(
    goBack: () -> Unit,
    uiState: SaveStepUIState,
    onEvent: (SaveStepUIEvent) -> Unit
) {
    BaseScaffold(
        isLoading = uiState.isLoading,
        topBar = { DefaultAppBar(title = "Edit step", mainAction = goBack) }) { innerPadding ->
        val (_, nameField, descriptionField, durationField) = uiState
        val commonModifier = Modifier.fillMaxWidth()
        val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
            if (isFocused) onEvent(SaveStepUIEvent.InputFieldFocusReceived(fieldName))
            else onEvent(SaveStepUIEvent.InputFieldFocusLost)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            TopLabeledTextField(
                inputField = nameField,
                onValueChange = { onEvent(SaveStepUIEvent.NameChange(it)) },
                label = stringResource(SharedRes.strings.step_name),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                modifier = commonModifier.onFocusChanged {
                    handleFocusChange(it.isFocused, nameField.name)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            TopLabeledTextField(
                inputField = descriptionField,
                onValueChange = { onEvent(SaveStepUIEvent.DescriptionChange(it)) },
                label = stringResource(SharedRes.strings.step_description),
                singleLine = false,
                modifier = commonModifier.height(150.dp).onFocusChanged {
                    handleFocusChange(it.isFocused, descriptionField.name)
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            TopLabeledTextField(
                inputField = durationField,
                onValueChange = { onEvent(SaveStepUIEvent.DurationChange(it)) },
                label = stringResource(SharedRes.strings.step_duration),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = { TextFieldTrailingText(text = stringResource(SharedRes.strings.months)) },
                modifier = commonModifier.onFocusChanged {
                    handleFocusChange(it.isFocused, durationField.name)
                }
            )

            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(onClick = {}) {
                Text(text = stringResource(SharedRes.strings.save))
            }
        }
    }
}