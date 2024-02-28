package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.editplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.LargeDropdownMenu
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.TextFieldTrailingText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan.CreatePlanResult
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.SlideUpTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.NumberCommaTransformation
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal data class EditPlanScreen(val plan: Plan) : Screen, KoinComponent,
    CustomScreenTransition by SlideUpTransition {
    private val viewModel: EditPlanViewModel by inject { parametersOf(plan) }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = viewModel.state
        val messageBarState = rememberMessageBarState()

        viewModel.editPlanResultChannel.receiveAsFlow().observerWithScreen {
            when (it) {
                is CreatePlanResult.Failure -> messageBarState.addError(it.message)
                CreatePlanResult.Success -> messageBarState.addSuccess("Plan edited successfully") {
                    navigator.pop()
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            goBack = navigator::pop
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: EditPlanUIState,
    onEvent: (EditPlanUiEvent) -> Unit,
    goBack: () -> Unit
) {
    BaseScaffold(
        messageBarState = messageBarState,
        isLoading = uiState.isLoading,
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.edit_plan),
                mainAction = goBack,
                mainIcon = Icons.Default.Close
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        val (_, sector, businessType, nameField, descriptionField, durationField, estimatedSellingPriceField, estimatedCostPriceField, sectors, businessTypes) = uiState
        val commonModifier = Modifier.fillMaxWidth()
        val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
            if (isFocused) onEvent(EditPlanUiEvent.InputFieldFocusReceived(fieldName))
            else onEvent(EditPlanUiEvent.InputFieldFocusLost)
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = horizontalScreenPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = verticalScreenPadding)
        ) {

            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_sector),
                items = sectors,
                itemToString = { it.value },
                selectedItem = sector,
                onItemSelected = { _, item -> onEvent(EditPlanUiEvent.SectorChange(item)) },
            )

            Spacer(modifier = Modifier.height(24.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_business_type),
                items = businessTypes,
                itemToString = { it.value },
                selectedItem = businessType,
                onItemSelected = { _, item -> onEvent(EditPlanUiEvent.BusinessType(item)) },
            )

            Spacer(modifier = Modifier.height(24.dp))
            TopLabeledTextField(
                inputField = nameField,
                onValueChange = { onEvent(EditPlanUiEvent.NameChange(it)) },
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
                onValueChange = { onEvent(EditPlanUiEvent.DescriptionChange(it)) },
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
                onValueChange = { onEvent(EditPlanUiEvent.DurationChange(it)) },
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
                onValueChange = { onEvent(EditPlanUiEvent.SellingPriceChange(it)) },
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
                onValueChange = { onEvent(EditPlanUiEvent.CostPriceChange(it)) },
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
            DefaultButton(onClick = { onEvent(EditPlanUiEvent.Submit) }) {
                Text(text = stringResource(SharedRes.strings.save))
            }
        }
    }
}