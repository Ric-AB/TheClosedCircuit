package com.closedcircuit.closedcircuitapplication.core.validation

class NameValidator : InputValidator {

    override fun validate(input: String): ValidationResult {
        if (input.length < 2) {
            return ValidationResult(
                isValid = false,
                validationErrors = listOf("Please enter at least two characters")
            )
        }

        return ValidationResult(isValid = true)
    }
}