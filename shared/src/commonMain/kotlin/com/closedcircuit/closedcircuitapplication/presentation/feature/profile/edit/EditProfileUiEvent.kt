package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit

sealed interface EditProfileUiEvent {
    data class FirstNameChange(val firstName: String) : EditProfileUiEvent
    data class LastNameChange(val lastName: String) : EditProfileUiEvent
    data class NickNameChange(val nickName: String) : EditProfileUiEvent
    data class PhoneNumberChange(val phoneNumber: String) : EditProfileUiEvent
    data class EmailChange(val email: String) : EditProfileUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : EditProfileUiEvent
    object InputFieldFocusLost : EditProfileUiEvent
    object OnSubmit : EditProfileUiEvent
}