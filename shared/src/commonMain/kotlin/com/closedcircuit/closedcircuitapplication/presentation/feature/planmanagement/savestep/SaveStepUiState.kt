package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.closedcircuit.closedcircuitapplication.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import com.closedcircuit.closedcircuitapplication.util.InputField
import com.closedcircuit.closedcircuitapplication.util.validation.AmountValidator
import com.closedcircuit.closedcircuitapplication.util.validation.TextValidator

data class SaveStepUIState(
    val isLoading: Boolean = false,
    val stepNameField: InputField = InputField(name = "stepName", validator = TextValidator()),
    val stepDescriptionField: InputField = InputField(
        name = "stepDescription",
        validator = TextValidator()
    ),
    val stepDurationField: InputField = InputField("stepDuration", validator = TextValidator()),
    val currentBudgetItem: BudgetItemState? = null,
    val budgetItemStates: SnapshotStateList<BudgetItemState> = mutableStateListOf()
) {

    val fieldsToValidate = listOf(stepNameField, stepDescriptionField, stepDurationField)
    val canSubmit: Boolean
        get() = budgetItemStates.isNotEmpty()

    companion object {
        fun init(step: Step?): SaveStepUIState {
            val initialState = SaveStepUIState()
            if (step == null) return initialState

            val nameField = initialState.stepNameField
            val descriptionField = initialState.stepDescriptionField
            val durationField = initialState.stepDurationField

            return SaveStepUIState(
                stepNameField = nameField.copy(inputValue = mutableStateOf(step.name)),
                stepDescriptionField = descriptionField.copy(inputValue = mutableStateOf(step.description)),
                stepDurationField = durationField.copy(inputValue = mutableStateOf(step.duration.value.toString())),
            )
        }
    }
}

data class BudgetItemState(
    val indexOfItem: Int = -1,
    val budget: Budget? = null,
    val budgetNameField: InputField = InputField(name = "budgetName", validator = TextValidator()),
    val budgetCostField: InputField = InputField(name = "budgetCost", validator = AmountValidator())
) {
    val fieldsToValidate = listOf(budgetNameField, budgetCostField)

    companion object {
        fun init(budget: Budget?): BudgetItemState {
            val initialState = BudgetItemState()
            if (budget == null) return initialState

            val nameField = initialState.budgetNameField
            val costField = initialState.budgetCostField

            return BudgetItemState(
                budget = budget,
                budgetNameField = nameField.copy(inputValue = mutableStateOf(budget.name)),
                budgetCostField = costField.copy(
                    inputValue = mutableStateOf(budget.cost.value.toInt().toString())
                )
            )
        }
    }
}

sealed interface SaveStepResult {
    object Success : SaveStepResult
    data class Failure(val message: String) : SaveStepResult
}
