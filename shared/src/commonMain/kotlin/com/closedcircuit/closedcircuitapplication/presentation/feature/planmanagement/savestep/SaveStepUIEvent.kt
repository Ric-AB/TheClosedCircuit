package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

sealed interface SaveStepUIEvent {

    data class NameChange(val name: String) : SaveStepUIEvent
    data class DescriptionChange(val description: String) : SaveStepUIEvent
    data class DurationChange(val duration: String) : SaveStepUIEvent
    data class InputFieldFocusReceived(val fieldName: String) : SaveStepUIEvent
    object InputFieldFocusLost : SaveStepUIEvent
    object Save : SaveStepUIEvent
}