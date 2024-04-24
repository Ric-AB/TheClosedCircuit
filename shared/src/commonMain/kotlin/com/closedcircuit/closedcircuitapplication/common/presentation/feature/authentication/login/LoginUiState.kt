package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login

import androidx.compose.runtime.mutableStateOf
import com.closedcircuit.closedcircuitapplication.common.util.InputField

data class LoginUIState(
    val emailField: InputField = InputField(inputValue = mutableStateOf("richardbajomo@yahoo.com")),
    val passwordField: InputField = InputField(inputValue = mutableStateOf("Password123_")),
    val isLoading: Boolean = false,
) {
    fun canSubmit(): Boolean {
        return emailField.value.isNotBlank() && passwordField.value.isNotEmpty()
    }
}

sealed interface LoginResult {
    object Success : LoginResult
    data class Failure(val message: String) : LoginResult
}
