package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.usecase.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ScreenModel {

    var state by mutableStateOf(RegisterUIState())
        private set

    private val _registerResultChannel = Channel<RegisterResult>()
    val resultResultChannel: ReceiveChannel<RegisterResult> = _registerResultChannel

    private var lastFocusedField: String? = null

    fun onEvent(event: RegisterUIEvent) {
        when (event) {
            is RegisterUIEvent.ConfirmPasswordChange -> updateConfirmPassword(event.confirmPassword)
            is RegisterUIEvent.EmailChange -> updateEmail(event.email)
            is RegisterUIEvent.FirstNameChange -> updateFirstName(event.firstName)
            is RegisterUIEvent.LastNameChange -> updateLastName(event.lastName)
            is RegisterUIEvent.NickNameChange -> updateNickName(event.nickName)
            is RegisterUIEvent.PasswordChange -> updatePassword(event.password)
            is RegisterUIEvent.PhoneNumberChange -> updatePhoneNumber(event.phoneNumber)
            is RegisterUIEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            RegisterUIEvent.InputFieldFocusLost -> validateField()
            RegisterUIEvent.Submit -> attemptRegistration()
        }
    }

    private fun attemptRegistration() {
        val isValid = areFieldsValid()
        if (isValid) {
            val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberField, passwordField, confirmPasswordField, _) = state
            val email = emailField.value.lowercase().trim()
            val fullName =
                "${firstNameField.value.trim()} ${nickNameField.value.trim()} ${lastNameField.value.trim()}"
            val phoneNumber = phoneNumberField.value.trim()
            val password = passwordField.value
            val confirmPassword = confirmPasswordField.value

            state = state.copy(loading = true)
            coroutineScope.launch {
                registerUseCase(
                    fullName,
                    email,
                    "Beneficiary",
                    phoneNumber,
                    password,
                    confirmPassword
                ).onSuccess {
                    state = state.copy(loading = false)
                    _registerResultChannel.send(RegisterResult.Success)
                }.onError { _, message ->
                    state = state.copy(loading = false)
                    _registerResultChannel.send(RegisterResult.Failure(message))
                }
            }
        }
    }

    private fun validateField() {
        if (lastFocusedField == null) return
        val fieldToValidate = state.fieldsToValidateAsList().find { it.name == lastFocusedField }
        fieldToValidate?.validateInput()
    }

    private fun areFieldsValid(): Boolean {
        val fields = state.fieldsToValidateAsList()
        fields.forEach { inputField -> inputField.validateInput() }
        validateConfirmPassword()
        return fields.all { inputField -> inputField.error.isEmpty() }
    }

    private fun updateLastFocusedField(fieldName: String) {
        lastFocusedField = fieldName
    }

    private fun updateFirstName(firstName: String) {
        state.firstNameField.onValueChange(firstName)
    }

    private fun updateNickName(nickName: String) {
        state.nickNameField.onValueChange(nickName)
    }

    private fun updateLastName(lastName: String) {
        state.lastNameField.onValueChange(lastName)
    }

    private fun updateEmail(email: String) {
        state.emailField.onValueChange(email)
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        state.phoneNumberField.onValueChange(phoneNumber)
    }

    private fun updatePassword(password: String) {
        state.passwordField.onValueChange(password)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        state.confirmPasswordField.onValueChange(confirmPassword)
        validateConfirmPassword()
    }

    private fun validateConfirmPassword() {
        if (state.confirmPasswordField.value != state.passwordField.value) {
            state.confirmPasswordField.error = "Passwords do not match"
        } else {
            state.confirmPasswordField.error = ""
        }
    }
}