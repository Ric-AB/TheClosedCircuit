package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

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
    data class Success(val activeProfile: ProfileType) : LoginResult
    data class Failure(val message: String) : LoginResult
}
