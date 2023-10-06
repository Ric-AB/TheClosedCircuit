@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.TextFieldTrailingText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultVerticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.NumberCommaTransformation
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal data class SaveStepScreen(val planID: ID, val step: Step? = null) : Screen, KoinComponent {
    private val viewModel: SaveStepViewModel by inject { parametersOf(planID, step) }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val messageBarState = rememberMessageBarState()
        val isNewStep = remember { step == null }

        viewModel.saveStepResult.receiveAsFlow().observerWithScreen {
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
    onEvent: (SaveStepUIEvent) -> Unit
) {
    val titleRes = remember {
        if (isNewStep) SharedRes.strings.create_step
        else SharedRes.strings.edit_step
    }

    BaseScaffold(
        messageBarState = messageBarState,
        isLoading = uiState.isLoading,
        topBar = { DefaultAppBar(title = stringResource(titleRes), mainAction = goBack) }
    ) { innerPadding ->
        val (_, nameField, descriptionField, durationField) = uiState
        val commonModifier = Modifier.fillMaxWidth()
        val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
            if (isFocused) onEvent(SaveStepUIEvent.InputFieldFocusReceived(fieldName))
            else onEvent(SaveStepUIEvent.InputFieldFocusLost)
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = defaultVerticalScreenPadding)
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            TopLabeledTextField(
                inputField = nameField,
                onValueChange = { onEvent(SaveStepUIEvent.StepNameChange(it)) },
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

            Spacer(modifier = Modifier.height(12.dp))
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
                    onClick = { onEvent(SaveStepUIEvent.InitializeBudgetItem) },
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
                uiState.budgetItemStates.forEachIndexed { index, budgetItemState ->
                    BudgetItem(
                        modifier = Modifier.fillMaxWidth(),
                        budgetItemState = budgetItemState,
                        onEditClick = { onEvent(SaveStepUIEvent.EditBudgetItem(index)) },
                        onDeleteClick = { onEvent(SaveStepUIEvent.RemoveBudgetItem(index)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(
                onClick = { onEvent(SaveStepUIEvent.SubmitAll) },
                enabled = uiState.canSubmit
            ) {
                Text(text = stringResource(SharedRes.strings.save))
            }
        }

        if (uiState.currentBudgetItem != null) {
            SaveStepModal(
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
                text = budgetItemState.budgetCostField.value,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

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
private fun SaveStepModal(
    bottomSheetState: SheetState,
    currentBudgetItemState: BudgetItemState,
    onEvent: (SaveStepUIEvent) -> Unit
) {
    val (indexOfItem, _, budgetNameField, budgetCostField) = currentBudgetItemState
    val commonModifier = Modifier.fillMaxWidth()
    val closeModal: () -> Unit = { onEvent(SaveStepUIEvent.ClearCurrentBudgetItem) }
    val handleFocusChange: (Boolean, String) -> Unit = { isFocused, fieldName ->
        if (isFocused) onEvent(SaveStepUIEvent.InputFieldFocusReceived(fieldName))
        else onEvent(SaveStepUIEvent.InputFieldFocusLost)
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
                onValueChange = { onEvent(SaveStepUIEvent.BudgetNameChange(it)) },
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
                onValueChange = { onEvent(SaveStepUIEvent.BudgetCostChange(it)) },
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
            DefaultButton(onClick = { onEvent(SaveStepUIEvent.SubmitBudgetItem) }) {
                Text(text = stringResource(SharedRes.strings.save))
            }
        }
    }
}