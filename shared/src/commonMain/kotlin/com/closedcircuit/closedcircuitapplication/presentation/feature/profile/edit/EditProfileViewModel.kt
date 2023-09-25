package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.model.Email
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val currentUser: User,
    private val userRepository: UserRepository
) : ScreenModel {

    var state: EditProfileUIState by mutableStateOf(EditProfileUIState.init(currentUser))
        private set

    private val _editProfileResult = Channel<EditProfileResult>()
    val editProfileResult: ReceiveChannel<EditProfileResult> = _editProfileResult

    private var lastFocusedField: String? = null

    fun onEvent(event: EditProfileUIEvent) {
        when (event) {
            is EditProfileUIEvent.EmailChange -> updateEmail(event.email)
            is EditProfileUIEvent.FirstNameChange -> updateFirstName(event.firstName)
            is EditProfileUIEvent.LastNameChange -> updateLastName(event.lastName)
            is EditProfileUIEvent.NickNameChange -> updateNickName(event.nickName)
            is EditProfileUIEvent.PhoneNumberChange -> updatePhoneNumber(event.phoneNumber)
            is EditProfileUIEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            EditProfileUIEvent.InputFieldFocusLost -> validateLastFocusedField()
            EditProfileUIEvent.OnSubmit -> attemptProfileEdit()
        }
    }

    private fun attemptProfileEdit() {
        if (areFieldsValid()) {
            val (firstNameField, nickNameField, lastNameField, emailField, phoneNumberField) = state
            val email = emailField.value.lowercase().trim()
            val fullName =
                "${firstNameField.value.trim()} ${nickNameField.value.trim()} ${lastNameField.value.trim()}"
            val phoneNumber = phoneNumberField.value.trim()

            state = state.copy(isLoading = true)
            coroutineScope.launch {
                val updatedUser = currentUser.copy(
                    fullName = Name(fullName),
                    email = Email(email),
                    phoneNumber = PhoneNumber(phoneNumber)
                )

                userRepository.updateUser(updatedUser)
                    .onSuccess {
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
            inputFields.forEach {inputField ->  inputField.validateInput() }
            inputFields.all {inputField ->  !inputField.isError }
        }
    }

    private fun validateLastFocusedField() {
        val fieldToValidate = state.fieldsToValidate.find { it.name == lastFocusedField }
        fieldToValidate?.validateInput()
    }
}