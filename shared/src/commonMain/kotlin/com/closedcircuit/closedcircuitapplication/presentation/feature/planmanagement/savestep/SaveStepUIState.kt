package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

import com.closedcircuit.closedcircuitapplication.util.InputField
import com.closedcircuit.closedcircuitapplication.util.validation.TextValidator

data class SaveStepUIState(
    val isLoading: Boolean = false,
    val nameField: InputField = InputField(name = "stepName", validator = TextValidator()),
    val descriptionField: InputField = InputField(
        name = "stepDescription",
        validator = TextValidator()
    ),
    val durationField: InputField = InputField("stepDuration", validator = TextValidator())
)
