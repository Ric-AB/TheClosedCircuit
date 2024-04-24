package com.closedcircuit.closedcircuitapplication.common.util.validation

class AmountValidator : InputValidator {

    override fun validate(input: String): ValidationResult {
        if (input.isEmpty()) {
            return ValidationResult(
                isValid = false,
                validationError = "Required *"
            )
        }

        try {
            val digit = input.toInt()
            if (digit <= 0) {
                return ValidationResult(
                    isValid = false,
                    validationError = "Please enter a value greater than zero"
                )
            }

        } catch (e: NumberFormatException) {
            return ValidationResult(
                isValid = false,
                validationError = "Only digit values are allowed"
            )
        }

        return ValidationResult(isValid = true)
    }
}