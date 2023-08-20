package com.closedcircuit.closedcircuitapplication.presentation.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.closedcircuit.closedcircuitapplication.core.validation.InputValidator

enum class ValidationType {
    Text, Phone, Email, Password
}

data class InputField(
    val inputValue: MutableState<String> = mutableStateOf(""),
    val validateOnChange: Boolean = false,
    val validator: InputValidator? = null
) {
    val errors = mutableStateListOf<String>()
    fun onValueChange(input: String) {
        inputValue.value = input

        if (!validateOnChange) errors.clear()

        if (validateOnChange && validator != null) {
            validateInput()
        }
    }

    fun validateInput() {
        if (validator == null) return

        val validationResult = validator.validate(inputValue.value)
        if (validationResult.isValid) {
            errors.clear()
        } else {
            errors.clear()
            errors.addAll(validationResult.validationErrors)
        }
    }
}
