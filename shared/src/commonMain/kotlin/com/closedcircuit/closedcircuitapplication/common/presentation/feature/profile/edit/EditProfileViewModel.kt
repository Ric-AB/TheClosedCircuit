package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.util.trimDuplicateSpace
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userRepository: UserRepository) : ScreenModel {

    private val currentUser = userRepository.userFlow.value!!
    var state: EditProfileUIState by mutableStateOf(EditProfileUIState.init(currentUser)); private set

    private val _editProfileResult = Channel<EditProfileResult>()
    val editProfileResult: ReceiveChannel<EditProfileResult> = _editProfileResult

    private var lastFocusedField: String? = null

    fun onEvent(event: EditProfileUiEvent) {
        when (event) {
            is EditProfileUiEvent.EmailChange -> updateEmail(event.email)
            is EditProfileUiEvent.FirstNameChange -> updateFirstName(event.firstName)
            is EditProfileUiEvent.LastNameChange -> updateLastName(event.lastName)
            is EditProfileUiEvent.NickNameChange -> updateNickName(event.nickName)
            is EditProfileUiEvent.PhoneNumberChange -> updatePhoneNumber(event.phoneNumber)
            is EditProfileUiEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            EditProfileUiEvent.InputFieldFocusLost -> validateLastFocusedField()
            EditProfileUiEvent.OnSubmit -> attemptProfileEdit()
        }
    }

    private fun attemptProfileEdit() {
        if (areFieldsValid()) {
            val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberField) = state
            val firstName = firstNameField.value.trim()
            val nickName = nickNameField.value.trim()
            val lastName = lastNameField.value.trim()
            val fullName = "$firstName $nickName $lastName".trimDuplicateSpace()
            val email = emailField.value.lowercase().trim()
            val phoneNumber = phoneNumberField.value.trim()

            screenModelScope.launch {
                state = state.copy(isLoading = true)
                val updatedUser = currentUser.copy(
                    fullName = Name(fullName),
                    email = Email(email),
                    phoneNumber = PhoneNumber(phoneNumber)
                )

                userRepository.updateUser(updatedUser)
                    .onComplete {
                        state = state.copy(isLoading = false)
                    }.onSuccess {
                        _editProfileResult.send(EditProfileResult.Success)
                    }.onError { _, message ->
                        _editProfileResult.send(EditProfileResult.Failure(message))
                    }
            }
        }
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

    private fun updateLastFocusedField(fieldName: String) {
        lastFocusedField = fieldName
    }

    private fun areFieldsValid(): Boolean {
        return state.fieldsToValidate.let { inputFields ->
            inputFields.forEach { inputField -> inputField.validateInput() }
            inputFields.all { inputField -> !inputField.isError }
        }
    }

    private fun validateLastFocusedField() {
        val fieldToValidate = state.fieldsToValidate.find { it.name == lastFocusedField }
        fieldToValidate?.validateInput()
    }
}