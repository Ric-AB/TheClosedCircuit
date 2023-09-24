package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

internal object PlanInfoScreen : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    @Composable
    override fun Content() {
        val viewModel =
            CreatePlanWrapperScreen.rememberScreenModel(tag = CreatePlanWrapperScreen.key) { CreatePlanViewModel() }


        ScreenContent(
            uiState = viewModel.state,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ScreenContent(uiState: CreatePlanUIState, onEvent: (CreatePlanUIEvent) -> Unit) {
    val (_, _, _, nameField, descriptionField, durationField, estimatedSellingPriceField, estimatedCostPriceField, _, _, _) = uiState
    Column(modifier = Modifier.fillMaxSize()) {
        TopLabeledTextField(
            value = nameField.value,
            onValueChange = { onEvent(CreatePlanUIEvent.NameChange(it)) },
            label = stringResource(SharedRes.strings.enter_plan_name)
        )

        Spacer(modifier = Modifier.height(20.dp))
        TopLabeledTextField(
            value = descriptionField.value,
            onValueChange = { onEvent(CreatePlanUIEvent.DescriptionChange(it)) },
            label = stringResource(SharedRes.strings.describe_your_plan),
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
        TopLabeledTextField(
            value = durationField.value,
            onValueChange = { onEvent(CreatePlanUIEvent.DurationChange(it)) },
            label = stringResource(SharedRes.strings.enter_plan_duration)
        )

        Spacer(modifier = Modifier.height(20.dp))
        TopLabeledTextField(
            value = estimatedSellingPriceField.value,
            onValueChange = { onEvent(CreatePlanUIEvent.SellingPriceChange(it)) },
            label = stringResource(SharedRes.strings.what_your_estimated_selling_price)
        )

        Spacer(modifier = Modifier.height(20.dp))
        TopLabeledTextField(
            value = estimatedCostPriceField.value,
            onValueChange = { onEvent(CreatePlanUIEvent.CostPriceChange(it)) },
            label = stringResource(SharedRes.strings.what_your_estimated_cost_price)
        )

        Spacer(modifier = Modifier.height(40.dp))
        DefaultButton(onClick = { onEvent(CreatePlanUIEvent.Submit) }) {
            Text(text = stringResource(SharedRes.strings.create_plan))
        }
    }
}

@Composable
private fun TopLabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes().medium,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
        focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface
    )
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        DefaultOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = null,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )
    }
}