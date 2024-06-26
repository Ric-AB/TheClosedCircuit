package com.closedcircuit.closedcircuitapplication.common.domain.util.validation

class NameValidator : InputValidator {

    override fun validate(input: String): ValidationResult {
        if (input.length < 2) {
            return ValidationResult(
                isValid = false,
                validationError = "Please enter at least two characters"
            )
        }

        return ValidationResult(isValid = true)
    }
}