package com.closedcircuit.closedcircuitapplication.core.validation

import com.closedcircuit.closedcircuitapplication.domain.value.Email

class EmailValidator : InputValidator {

    override fun validate(input: String): ValidationResult {
        if (!Email.emailRegex.matches(input)) {
            return ValidationResult(
                isValid = false,
                validationErrors = listOf("Please enter a valid email")
            )
        }

        return ValidationResult(isValid = true)
    }
}