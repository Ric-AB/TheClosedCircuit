package com.closedcircuit.closedcircuitapplication.util.validation

class TextValidator : InputValidator {

    override fun validate(input: String): ValidationResult {
        if (input.isEmpty()) {
            return ValidationResult(isValid = false, "Required *")
        }

        if (input.isBlank()) {
            return ValidationResult(isValid = false, "Please enter non-space characters")
        }
        return ValidationResult(isValid = true)
    }
}