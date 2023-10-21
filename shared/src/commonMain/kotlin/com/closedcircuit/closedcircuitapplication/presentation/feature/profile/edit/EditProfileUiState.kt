package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit

import androidx.compose.runtime.mutableStateOf
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.util.InputField
import com.closedcircuit.closedcircuitapplication.util.validation.EmailValidator
import com.closedcircuit.closedcircuitapplication.util.validation.NameValidator
import com.closedcircuit.closedcircuitapplication.util.validation.PhoneNumberValidator

data class EditProfileUIState(
    val firstNameField: InputField,
    val nickNameField: InputField,
    val lastNameField: InputField,
    val emailField: InputField,
    val phoneNumberField: InputField,
    val isLoading: Boolean
) {
    companion object {
        fun init(user: User): EditProfileUIState {
            return EditProfileUIState(
                firstNameField = InputField(
                    inputValue = mutableStateOf(user.firstName.value),
                    name = "firstName",
                    validator = NameValidator()
                ),

                nickNameField = InputField(
                    inputValue = mutableStateOf(user.preferredName?.value ?: ""),
                    name = "nickName"
                ),

                lastNameField = InputField(
                    inputValue = mutableStateOf(user.lastName.value),
                    name = "lastName",
                    validator = NameValidator()
                ),

                emailField = InputField(
                    inputValue = mutableStateOf(user.email.value),
                    name = "email",
                    validator = EmailValidator()
                ),

                phoneNumberField = InputField(
                    inputValue = mutableStateOf(user.phoneNumber.value),
                    name = "phoneNumber",
                    validator = PhoneNumberValidator()
                ),

                isLoading = false
            )
        }
    }

    val fieldsToValidate = listOf(
        firstNameField,
        lastNameField,
        emailField,
        phoneNumberField,
    )
}

sealed interface EditProfileResult {
    object Success : EditProfileResult

    data class Failure(val message: String) : EditProfileResult
}
