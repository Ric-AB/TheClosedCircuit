package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.TextFieldTrailingText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.NumberCommaTransformation
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object PlanInfoScreen : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    private val createPlanUseCase: CreatePlanUseCase by inject()

    @Composable
    override fun Content() {
        val viewModel =
            CreatePlanWrapperScreen.rememberScreenModel { CreatePlanViewModel(createPlanUseCase) }


        ScreenContent(
            uiState = viewModel.state,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ScreenContent(uiState: CreatePlanUIState, onEvent: (CreatePlanUIEvent) -> Unit) {
    val (_, _, _, _, nameField, descriptionField, durationField, estimatedSellingPriceField, estimatedCostPriceField, _, _, _) = uiState
    val commonModifier = Modifier.fillMaxWidth()
    val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
        if (isFocused) onEvent(CreatePlanUIEvent.InputFieldFocusReceived(fieldName))
        else onEvent(CreatePlanUIEvent.InputFieldFocusLost)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopLabeledTextField(
            inputField = nameField,
            onValueChange = { onEvent(CreatePlanUIEvent.NameChange(it)) },
            label = stringResource(SharedRes.strings.enter_plan_name),
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
            onValueChange = { onEvent(CreatePlanUIEvent.DescriptionChange(it)) },
            label = stringResource(SharedRes.strings.describe_your_plan),
            singleLine = false,
            modifier = commonModifier.height(150.dp).onFocusChanged {
                handleFocusChange(it.isFocused, descriptionField.name)
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(40.dp))
        TopLabeledTextField(
            inputField = durationField,
            onValueChange = { onEvent(CreatePlanUIEvent.DurationChange(it)) },
            label = stringResource(SharedRes.strings.enter_plan_duration),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            trailingIcon = { TextFieldTrailingText(text = stringResource(SharedRes.strings.months)) },
            modifier = commonModifier.onFocusChanged {
                handleFocusChange(it.isFocused, durationField.name)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        TopLabeledTextField(
            inputField = estimatedSellingPriceField,
            onValueChange = { onEvent(CreatePlanUIEvent.SellingPriceChange(it)) },
            label = stringResource(SharedRes.strings.what_your_estimated_selling_price),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            visualTransformation = NumberCommaTransformation(),
            trailingIcon = { TextFieldTrailingText(text = "NGN") },
            modifier = commonModifier.onFocusChanged {
                handleFocusChange(it.isFocused, estimatedSellingPriceField.name)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        TopLabeledTextField(
            inputField = estimatedCostPriceField,
            onValueChange = { onEvent(CreatePlanUIEvent.CostPriceChange(it)) },
            label = stringResource(SharedRes.strings.what_your_estimated_cost_price),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            visualTransformation = NumberCommaTransformation(),
            trailingIcon = { TextFieldTrailingText(text = "NGN") },
            modifier = commonModifier.onFocusChanged {
                handleFocusChange(it.isFocused, estimatedCostPriceField.name)
            }
        )

        Spacer(modifier = Modifier.height(40.dp))
        DefaultButton(onClick = { onEvent(CreatePlanUIEvent.Submit) }) {
            Text(text = stringResource(SharedRes.strings.create_plan))
        }
    }
}