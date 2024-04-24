package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep

sealed interface SaveStepUiEvent {

    data class StepNameChange(val name: String) : SaveStepUiEvent
    data class DescriptionChange(val description: String) : SaveStepUiEvent
    data class DurationChange(val duration: String) : SaveStepUiEvent
    data class BudgetNameChange(val name: String) : SaveStepUiEvent
    data class BudgetCostChange(val cost: String) : SaveStepUiEvent
    data class EditBudgetItem(val index: Int) : SaveStepUiEvent
    data class RemoveBudgetItem(val index: Int): SaveStepUiEvent
    object InitializeBudgetItem : SaveStepUiEvent
    object ClearCurrentBudgetItem : SaveStepUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : SaveStepUiEvent
    object InputFieldFocusLost : SaveStepUiEvent
    object SubmitBudgetItem : SaveStepUiEvent
    object SubmitAll : SaveStepUiEvent
}