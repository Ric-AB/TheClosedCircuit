package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.changepassword

import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.PasswordValidator
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val oldPasswordField: InputField = InputField(name = "oldPassword"),
    val newPasswordField: InputField = InputField(
        name = "newPassword",
        validator = PasswordValidator()
    ),
    val confirmPasswordField: InputField = InputField(name = "confirmPassword")
) {
    val fieldsToValidate = listOf(
        oldPasswordField,
        newPasswordField,
        confirmPasswordField
    )

    val canSubmit: Boolean
        get() {
            val oldPasswordValid = oldPasswordField.value.isNotEmpty()
            val newPasswordValid = !newPasswordField.isError
            val confirmPasswordValid = !confirmPasswordField.isError
            return oldPasswordValid && newPasswordValid && confirmPasswordValid
        }
}


sealed interface ChangePasswordResult {
    object ChangePasswordSuccess : ChangePasswordResult

    data class ChangePasswordError(val message: String) : ChangePasswordResult
}