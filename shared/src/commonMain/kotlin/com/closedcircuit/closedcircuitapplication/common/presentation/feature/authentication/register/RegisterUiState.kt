package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register

import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.EmailValidator
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.NameValidator
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.PasswordValidator
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.PhoneNumberValidator
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

data class RegisterUIState(
    val firstNameField: InputField = InputField(name = "firstName", validator = NameValidator()),
    val nickNameField: InputField = InputField(name = "nickName"),
    val lastNameField: InputField = InputField(name = "lastName", validator = NameValidator()),
    val emailField: InputField = InputField(name = "email", validator = EmailValidator()),
    val phoneNumberField: InputField = InputField(
        name = "phoneNumber",
        validator = PhoneNumberValidator()
    ),

    val passwordField: InputField = InputField(
        name = "password",
        validateOnChange = true,
        validator = PasswordValidator()
    ),

    val confirmPasswordField: InputField = InputField(name = "confirmPassword"),
    val isLoading: Boolean = false,
) {
    val fieldsToValidate = listOf(
        firstNameField,
        lastNameField,
        emailField,
        phoneNumberField,
        passwordField,
        confirmPasswordField
    )
}

sealed interface RegisterResult {
    object Success : RegisterResult
    data class Failure(val message: String) : RegisterResult
}
