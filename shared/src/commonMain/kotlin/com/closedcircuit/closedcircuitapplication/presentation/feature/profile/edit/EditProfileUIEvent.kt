package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.edit

sealed interface EditProfileUIEvent {
    data class FirstNameChange(val firstName: String) : EditProfileUIEvent
    data class LastNameChange(val lastName: String) : EditProfileUIEvent
    data class NickNameChange(val nickName: String) : EditProfileUIEvent
    data class PhoneNumberChange(val phoneNumber: String) : EditProfileUIEvent
    data class EmailChange(val email: String) : EditProfileUIEvent
    data class InputFieldFocusReceived(val fieldName: String) : EditProfileUIEvent
    object InputFieldFocusLost : EditProfileUIEvent
    object OnSubmit : EditProfileUIEvent
}