package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class RegisterViewModel : ScreenModel {

    var state by mutableStateOf(RegisterUIState())
        private set

    fun onEvent(event: RegisterUIEvent) {
        when (event) {
            is RegisterUIEvent.ConfirmPasswordChange -> updateConfirmPassword(event.confirmPassword)
            is RegisterUIEvent.EmailChange -> updateEmail(event.email)
            is RegisterUIEvent.FirstNameChange -> updateFirstName(event.firstName)
            is RegisterUIEvent.LastNameChange -> updateLastName(event.lastName)
            is RegisterUIEvent.NickNameChange -> updateNickName(event.nickName)
            is RegisterUIEvent.PasswordChange -> updatePassword(event.password)
            is RegisterUIEvent.PhoneNumberChange -> updatePhoneNumber(event.phoneNumber)
        }
    }

    private fun updateFirstName(firstName: String) {
        state = state.copy(firstName = firstName)
    }

    private fun updateNickName(nickName: String) {
        state = state.copy(nickName = nickName)
    }

    private fun updateLastName(lastName: String) {
        state = state.copy(lastName = lastName)
    }

    private fun updateEmail(email: String) {
        state = state.copy(email = email)
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        state = state.copy(phoneNumber = phoneNumber)
    }

    private fun updatePassword(password: String) {
        state = state.copy(password = password)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
    }
}