package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

sealed interface SaveStepUIEvent {

    data class StepNameChange(val name: String) : SaveStepUIEvent
    data class DescriptionChange(val description: String) : SaveStepUIEvent
    data class DurationChange(val duration: String) : SaveStepUIEvent
    data class BudgetNameChange(val name: String) : SaveStepUIEvent
    data class BudgetCostChange(val cost: String) : SaveStepUIEvent
    data class EditBudgetItem(val index: Int) : SaveStepUIEvent
    data class RemoveBudgetItem(val index: Int): SaveStepUIEvent
    object InitializeBudgetItem : SaveStepUIEvent
    object ClearCurrentBudgetItem : SaveStepUIEvent
    data class InputFieldFocusReceived(val fieldName: String) : SaveStepUIEvent
    object InputFieldFocusLost : SaveStepUIEvent
    object SubmitBudgetItem : SaveStepUIEvent
    object SubmitAll : SaveStepUIEvent
}