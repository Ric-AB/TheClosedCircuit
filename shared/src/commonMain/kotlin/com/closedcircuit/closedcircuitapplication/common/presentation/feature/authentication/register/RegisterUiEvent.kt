package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register

sealed interface RegisterUiEvent {

    data class FirstNameChange(val firstName: String) : RegisterUiEvent
    data class LastNameChange(val lastName: String) : RegisterUiEvent
    data class NickNameChange(val nickName: String) : RegisterUiEvent
    data class PhoneNumberChange(val phoneNumber: String) : RegisterUiEvent
    data class EmailChange(val email: String) : RegisterUiEvent
    data class PasswordChange(val password: String) : RegisterUiEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : RegisterUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : RegisterUiEvent
    object InputFieldFocusLost : RegisterUiEvent
    object Submit : RegisterUiEvent
}