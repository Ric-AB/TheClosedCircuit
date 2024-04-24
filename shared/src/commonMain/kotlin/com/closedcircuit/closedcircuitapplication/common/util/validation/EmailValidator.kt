package com.closedcircuit.closedcircuitapplication.common.util.validation

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Email

class EmailValidator : InputValidator {

    override fun validate(input: String): ValidationResult {
        if (!Email.emailRegex.matches(input)) {
            return ValidationResult(
                isValid = false,
                validationError = "Please enter a valid email"
            )
        }

        return ValidationResult(isValid = true)
    }
}