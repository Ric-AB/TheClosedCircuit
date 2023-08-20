package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

import com.closedcircuit.closedcircuitapplication.presentation.util.InputField

data class LoginUIState(
    val emailField: InputField = InputField(),
    val passwordField: InputField = InputField(),
    val loading: Boolean = false,
) {
    fun canSubmit(): Boolean {
        return emailField.inputValue.value.isNotBlank() && passwordField.inputValue.value.isNotEmpty()
    }
}

sealed interface LoginResult {
    object Success : LoginResult
    data class Failure(val message: String) : LoginResult
}
