package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.savestep

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class SaveStepViewModel : ScreenModel {

    var state by mutableStateOf(SaveStepUIState())


    fun onEvent(event: SaveStepUIEvent) {
        when (event) {
            is SaveStepUIEvent.DescriptionChange -> TODO()
            is SaveStepUIEvent.DurationChange -> TODO()
            is SaveStepUIEvent.NameChange -> TODO()
            is SaveStepUIEvent.InputFieldFocusReceived -> TODO()
            SaveStepUIEvent.InputFieldFocusLost -> TODO()
            SaveStepUIEvent.Save -> TODO()
        }
    }
}