package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

import com.closedcircuit.closedcircuitapplication.util.InputField

data class LoginUIState(
    val emailField: InputField = InputField(),
    val passwordField: InputField = InputField(),
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
