package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.country.CountryRepository
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.RegisterWithLoginUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.PhoneNumberValidator
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PhoneNumberState
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.util.trimDuplicateSpace
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerWithLoginUseCase: RegisterWithLoginUseCase,
    private val appSettingsRepository: AppSettingsRepository,
    private val countryRepository: CountryRepository
) : ScreenModel {

    lateinit var state: MutableState<RegisterUIState>

    private val _registerResultChannel = Channel<RegisterResult>()
    val registerResultChannel: ReceiveChannel<RegisterResult> = _registerResultChannel

    private var lastFocusedField: String? = null

    init {
        initState()
    }

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.ConfirmPasswordChange -> updateConfirmPassword(event.confirmPassword)
            is RegisterUiEvent.EmailChange -> updateEmail(event.email)
            is RegisterUiEvent.FirstNameChange -> updateFirstName(event.firstName)
            is RegisterUiEvent.LastNameChange -> updateLastName(event.lastName)
            is RegisterUiEvent.NickNameChange -> updateNickName(event.nickName)
            is RegisterUiEvent.PasswordChange -> updatePassword(event.password)
            is RegisterUiEvent.PhoneStateChange -> updatePhoneState(event.phoneState)
            is RegisterUiEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            RegisterUiEvent.InputFieldFocusLost -> validateLastFocusedField()
            RegisterUiEvent.Submit -> attemptRegistration()
        }
    }

    private fun initState() {
        val phoneNumberState = PhoneNumberState(
            inputField = InputField(
                name = "phone",
                validator = PhoneNumberValidator()
            ),
            country = countryRepository.getDefaultCountry(),
            countryOptions = countryRepository.getAvailableCountries().toImmutableList()
        )
        state = mutableStateOf(RegisterUIState(phoneNumberState = phoneNumberState))
    }

    private fun attemptRegistration() {
        if (areFieldsValid()) {
            val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberState, passwordField, confirmPasswordField, _) = state.value
            val email = emailField.value.lowercase().trim()
            val firstName = firstNameField.value.trim()
            val nickName = nickNameField.value.trim()
            val lastName = lastNameField.value.trim()
            val fullName = "$firstName $nickName $lastName".trimDuplicateSpace()
            val phoneNumber = phoneNumberState.getPhoneNumberWithCode().trim()
            val password = passwordField.value
            val confirmPassword = confirmPasswordField.value

            state.value = state.value.copy(isLoading = true)
            screenModelScope.launch {
                registerWithLoginUseCase(
                    fullName = fullName,
                    email = email,
                    roles = "Beneficiary",
                    phoneNumber = phoneNumber,
                    password = password,
                    confirmPassword = confirmPassword
                ).onSuccess {
                    val activeProfile = appSettingsRepository.getActiveProfile()
                    state.value = state.value.copy(isLoading = false)
                    _registerResultChannel.send(RegisterResult.Success(activeProfile))
                }.onError { _, message ->
                    state.value = state.value.copy(isLoading = false)
                    _registerResultChannel.send(RegisterResult.Failure(message))
                }
            }
        }
    }

    private fun validateLastFocusedField() {
        val fieldToValidate = state.value.fieldsToValidate.find { it.name == lastFocusedField }
        fieldToValidate?.validateInput()
    }

    private fun areFieldsValid(): Boolean {
        val fields = state.value.fieldsToValidate
        fields.forEach { inputField -> inputField.validateInput() }
        validateConfirmPassword()
        return fields.all { inputField -> !inputField.isError }
    }

    private fun updateLastFocusedField(fieldName: String) {
        lastFocusedField = fieldName
    }

    private fun updateFirstName(firstName: String) {
        state.value.firstNameField.onValueChange(firstName)
    }

    private fun updateNickName(nickName: String) {
        state.value.nickNameField.onValueChange(nickName)
    }

    private fun updateLastName(lastName: String) {
        state.value.lastNameField.onValueChange(lastName)
    }

    private fun updateEmail(email: String) {
        state.value.emailField.onValueChange(email)
    }

    private fun updatePhoneState(phoneState: PhoneNumberState) {
        state.value = state.value.copy(phoneNumberState = phoneState)
    }

    private fun updatePassword(password: String) {
        state.value.passwordField.onValueChange(password)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        state.value.confirmPasswordField.onValueChange(confirmPassword)
        validateConfirmPassword()
    }

    private fun validateConfirmPassword() {
        if (state.value.confirmPasswordField.value != state.value.passwordField.value) {
            state.value.confirmPasswordField.error = "Passwords do not match"
        } else {
            state.value.confirmPasswordField.error = ""
        }
    }
}