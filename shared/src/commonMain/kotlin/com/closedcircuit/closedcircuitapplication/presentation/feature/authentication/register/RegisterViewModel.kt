package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ScreenModel {

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
            RegisterUIEvent.Submit -> attemptRegistration()
            RegisterUIEvent.RegisterResultHandled -> updateRegisterResult()
        }
    }

    private fun attemptRegistration() {
        val email = state.email.lowercase().trim()
        val fullName = "${state.firstName.trim()} ${state.nickName.trim()} ${state.lastName.trim()}"
        val phoneNumber = state.phoneNumber.trim()
        val password = state.password
        val confirmPassword = state.confirmPassword

        state = state.copy(loading = true)
        coroutineScope.launch {
            registerUseCase.invoke(
                fullName,
                email,
                "Beneficiary",
                phoneNumber,
                password,
                confirmPassword
            ).onSuccess {
                state = state.copy(loading = false, registerResult = RegisterResult.Success)
            }.onError { _, message ->
                state =
                    state.copy(loading = false, registerResult = RegisterResult.Failure(message))
            }
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

    private fun updateRegisterResult() {
        state = state.copy(registerResult = null)
    }
}