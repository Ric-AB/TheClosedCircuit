package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.edit

import com.closedcircuit.closedcircuitapplication.common.presentation.component.PhoneNumberState

sealed interface EditProfileUiEvent {
    data class FirstNameChange(val firstName: String) : EditProfileUiEvent
    data class LastNameChange(val lastName: String) : EditProfileUiEvent
    data class NickNameChange(val nickName: String) : EditProfileUiEvent
    data class PhoneStateChange(val phoneState: PhoneNumberState) : EditProfileUiEvent
    data class EmailChange(val email: String) : EditProfileUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : EditProfileUiEvent
    object InputFieldFocusLost : EditProfileUiEvent
    object OnSubmit : EditProfileUiEvent
}