@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Shapes
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TextFieldAffix
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideUpTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.NumberCommaTransformation
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

internal data class SaveStepScreen(val planId: ID, val step: Step? = null) : Screen, KoinComponent,
    CustomScreenTransition by SlideUpTransition {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SaveStepViewModel> { parametersOf(planId, step) }
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val messageBarState = rememberMessageBarState()
        val isNewStep = remember { step == null }

        viewModel.saveStepResult.receiveAsFlow().observeWithScreen {
            when (it) {
                is SaveStepResult.Failure -> messageBarState.addError(it.message)
                SaveStepResult.Success -> {
                    val message = if (step == null) "Step created successfully"
                    else "Step updated successfully"

                    messageBarState.addSuccess(message) {
                        navigator.pop()
                    }
                }
            }
        }

        ScreenContent(
            isNewStep = isNewStep,
            messageBarState = messageBarState,
            uiState = viewModel.state,
            bottomSheetState = bottomSheetState,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ScreenContent(
    isNewStep: Boolean,
    messageBarState: MessageBarState,
    uiState: SaveStepUIState,
    bottomSheetState: SheetState,
    goBack: () -> Unit,
    onEvent: (SaveStepUiEvent) -> Unit
) {
    val titleRes = remember {
        if (isNewStep) SharedRes.strings.create_step
        else SharedRes.strings.edit_step
    }

    BaseScaffold(
        messageBarState = messageBarState,
        showLoadingDialog = uiState.loading,
        topBar = { DefaultAppBar(title = stringResource(titleRes), mainAction = goBack) },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        val (_, nameField, descriptionField, durationField) = uiState
        val commonModifier = Modifier.fillMaxWidth()
        val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
            if (isFocused) onEvent(SaveStepUiEvent.InputFieldFocusReceived(fieldName))
            else onEvent(SaveStepUiEvent.InputFieldFocusLost)
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = horizontalScreenPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = verticalScreenPadding)
        ) {
            TopLabeledTextField(
                inputField = nameField,
                onValueChange = { onEvent(SaveStepUiEvent.StepNameChange(it)) },
                label = stringResource(SharedRes.strings.step_name),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                modifier = commonModifier.onFocusChanged {
                    handleFocusChange(it.isFocused, nameField.name)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            TopLabeledTextField(
                inputField = descriptionField,
                onValueChange = { onEvent(SaveStepUiEvent.DescriptionChange(it)) },
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

            Spacer(modifier = Modifier.height(12.dp))
            TopLabeledTextField(
                inputField = durationField,
                onValueChange = { onEvent(SaveStepUiEvent.DurationChange(it)) },
                label = stringResource(SharedRes.strings.step_duration),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = { TextFieldAffix(text = stringResource(SharedRes.strings.months)) },
                modifier = commonModifier.onFocusChanged {
                    handleFocusChange(it.isFocused, durationField.name)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(SharedRes.strings.budget_items),
                    style = MaterialTheme.typography.labelMedium
                )

                TextButton(
                    onClick = { onEvent(SaveStepUiEvent.InitializeBudgetItem) },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(SharedRes.strings.add_budget),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.budgetItemsState.forEachIndexed { index, budgetItemState ->
                    BudgetItem(
                        modifier = Modifier.fillMaxWidth(),
                        budgetItemState = budgetItemState,
                        onEditClick = { onEvent(SaveStepUiEvent.EditBudgetItem(index)) },
                        onDeleteClick = { onEvent(SaveStepUiEvent.RemoveBudgetItem(index)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(
                onClick = { onEvent(SaveStepUiEvent.SubmitAll) },
                enabled = uiState.canSubmit
            ) {
                Text(text = stringResource(SharedRes.strings.save))
            }
        }

        if (uiState.currentBudgetItem != null) {
            SaveBudgetModal(
                bottomSheetState = bottomSheetState,
                currentBudgetItemState = uiState.currentBudgetItem,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun BudgetItem(
    modifier: Modifier,
    budgetItemState: BudgetItemState,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formattedAmount = remember(budgetItemState.budgetCostField.value) {
        Amount(budgetItemState.budgetCostField.value.toDouble()).getFormattedValue()
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
        ) {
            Text(
                text = budgetItemState.budgetNameField.value,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = formattedAmount,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            val textButtonStyle = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp)
            val textButtonIconModifier = Modifier.size(18.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onErrorContainer)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "delete budget",
                            modifier = textButtonIconModifier
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Delete",
                            style = textButtonStyle,
                        )
                    }
                }

                TextButton(onClick = onEditClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "edit budget",
                            modifier = textButtonIconModifier
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            style = textButtonStyle,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SaveBudgetModal(
    bottomSheetState: SheetState,
    currentBudgetItemState: BudgetItemState,
    onEvent: (SaveStepUiEvent) -> Unit
) {
    val (indexOfItem, _, budgetNameField, budgetCostField) = currentBudgetItemState
    val commonModifier = Modifier.fillMaxWidth()
    val closeModal: () -> Unit = { onEvent(SaveStepUiEvent.ClearCurrentBudgetItem) }
    val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
        if (isFocused) onEvent(SaveStepUiEvent.InputFieldFocusReceived(fieldName))
        else onEvent(SaveStepUiEvent.InputFieldFocusLost)
    }

    val titleResource = if (indexOfItem < 0) SharedRes.strings.add_budget
    else SharedRes.strings.edit_budget

    ModalBottomSheet(
        onDismissRequest = closeModal,
        sheetState = bottomSheetState,
        shape = Shapes().small,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(titleResource),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = closeModal) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "close modal")
                }
            }

            TopLabeledTextField(
                inputField = budgetNameField,
                onValueChange = { onEvent(SaveStepUiEvent.BudgetNameChange(it)) },
                label = stringResource(SharedRes.strings.budget_name),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = commonModifier.onFocusChanged {
                    handleFocusChange(it.isFocused, budgetNameField.name)
                },
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(8.dp))
            TopLabeledTextField(
                inputField = budgetCostField,
                onValueChange = { onEvent(SaveStepUiEvent.BudgetCostChange(it)) },
                label = stringResource(SharedRes.strings.budget_cost),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = NumberCommaTransformation(),
                modifier = commonModifier.onFocusChanged {
                    handleFocusChange(it.isFocused, budgetCostField.name)
                },
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(onClick = { onEvent(SaveStepUiEvent.SubmitBudgetItem) }) {
                Text(text = stringResource(SharedRes.strings.save))
            }
        }
    }
}