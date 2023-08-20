package com.closedcircuit.closedcircuitapplication.core.validation

class PhoneNumberValidator : InputValidator {

    override fun validate(input: String): ValidationResult {


        return ValidationResult(isValid = true)
    }
}