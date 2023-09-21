package com.closedcircuit.closedcircuitapplication.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.closedcircuit.closedcircuitapplication.core.validation.InputValidator


data class InputField(
    val name: String = "",
    val inputValue: MutableState<String> = mutableStateOf(""),
    val validateOnChange: Boolean = false,
    val validator: InputValidator? = null
) {
    val value by inputValue
    var error by mutableStateOf("")
    val isError: Boolean
        get() = error.isNotEmpty()


    fun onValueChange(input: String) {
        inputValue.value = input

        if (!validateOnChange) error = ""

        if (validateOnChange && validator != null) {
            validateInput()
        }
    }

    fun validateInput() {
        if (validator == null) return

        val validationResult = validator.validate(inputValue.value)
        error = if (validationResult.isValid) {
            ""
        } else {
            validationResult.validationError
        }
    }
}
