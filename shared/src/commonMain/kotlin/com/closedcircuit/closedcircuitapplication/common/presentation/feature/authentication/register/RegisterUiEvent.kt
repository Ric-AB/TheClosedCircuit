package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register

import com.closedcircuit.closedcircuitapplication.common.domain.country.Country
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PhoneNumberState

sealed interface RegisterUiEvent {

    data class FirstNameChange(val firstName: String) : RegisterUiEvent
    data class LastNameChange(val lastName: String) : RegisterUiEvent
    data class NickNameChange(val nickName: String) : RegisterUiEvent
    data class PhoneStateChange(val phoneState: PhoneNumberState) : RegisterUiEvent
    data class EmailChange(val email: String) : RegisterUiEvent
    data class PasswordChange(val password: String) : RegisterUiEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : RegisterUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : RegisterUiEvent
    object InputFieldFocusLost : RegisterUiEvent
    object Submit : RegisterUiEvent
}