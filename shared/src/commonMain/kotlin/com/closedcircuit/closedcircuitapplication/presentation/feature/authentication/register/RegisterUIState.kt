package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

data class RegisterUIState(
    val firstName: String = "",
    val nickName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)
