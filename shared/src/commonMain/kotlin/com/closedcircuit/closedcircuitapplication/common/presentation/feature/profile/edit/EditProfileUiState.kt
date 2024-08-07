package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.edit

import androidx.compose.runtime.mutableStateOf
import com.closedcircuit.closedcircuitapplication.common.domain.user.User
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.EmailValidator
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.NameValidator
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.PhoneNumberValidator
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PhoneNumberState
import kotlinx.collections.immutable.persistentListOf

data class EditProfileUIState(
    val firstNameField: InputField,
    val nickNameField: InputField,
    val lastNameField: InputField,
    val emailField: InputField,
    val phoneNumberState: PhoneNumberState,
    val isLoading: Boolean
) {
    companion object {
        fun init(user: User): EditProfileUIState {
            val phoneNumberState = PhoneNumberState(
                inputField = InputField(
                    inputValue = mutableStateOf(user.phoneNumber.value),
                    name = "phoneNumber",
                    validator = PhoneNumberValidator()
                ),
                country = user.country,
                countryOptions = persistentListOf()
            )

            return EditProfileUIState(
                firstNameField = InputField(
                    inputValue = mutableStateOf(user.firstName.value),
                    name = "firstName",
                    validator = NameValidator()
                ),
                nickNameField = InputField(
                    inputValue = mutableStateOf(user.preferredName?.value.orEmpty()),
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
                phoneNumberState = phoneNumberState,
                isLoading = false
            )
        }
    }

    val fieldsToValidate = listOf(
        firstNameField,
        lastNameField,
        emailField,
        phoneNumberState.inputField,
    )
}

sealed interface EditProfileResult {
    object Success : EditProfileResult

    data class Failure(val message: String) : EditProfileResult
}
