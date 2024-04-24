package com.closedcircuit.closedcircuitapplication.common.util.validation

class PhoneNumberValidator : InputValidator {

    override fun validate(input: String): ValidationResult {


        return ValidationResult(isValid = true)
    }
}