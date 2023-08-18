package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

sealed interface RegisterUIEvent {

    data class FirstNameChange(val firstName: String) : RegisterUIEvent
    data class LastNameChange(val lastName: String) : RegisterUIEvent
    data class NickNameChange(val nickName: String) : RegisterUIEvent
    data class PhoneNumberChange(val phoneNumber: String) : RegisterUIEvent
    data class EmailChange(val email: String) : RegisterUIEvent
    data class PasswordChange(val password: String) : RegisterUIEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : RegisterUIEvent
    object Submit : RegisterUIEvent
    object RegisterResultHandled : RegisterUIEvent
}