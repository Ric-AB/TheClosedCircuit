package com.closedcircuit.closedcircuitapplication.core.validation

enum class PasswordCriteria {
    Length, Case, Digit, SpecialCharacter
}

class PasswordValidator : InputValidator {
    private val minPasswordLength = 8
    private val uppercaseRegex = Regex("[A-Z]")
    private val lowercaseRegex = Regex("[a-z]")
    private val digitRegex = Regex("[0-9]")
    private val specialCharacterRegex = Regex("[@#\$%^&+=*_!-]")

    companion object {
        val criteriaMessage = mapOf(
            PasswordCriteria.Length to "* Minimum of 8 characters",
            PasswordCriteria.Case to "* Uppercase and lowercase",
            PasswordCriteria.Digit to "* Numbers",
            PasswordCriteria.SpecialCharacter to "* Special characters"
        )
    }

    override fun validate(input: String): ValidationResult {

        val validationErrors = StringBuilder()
        if (input.length < minPasswordLength) {
            validationErrors.append(criteriaMessage[PasswordCriteria.Length]!!)
        }

        if (!input.contains(uppercaseRegex) || !input.contains(lowercaseRegex)) {
            validationErrors.append("\n")
            validationErrors.append(criteriaMessage[PasswordCriteria.Case]!!)
        }

        if (!input.contains(digitRegex)) {
            validationErrors.append("\n")
            validationErrors.append(criteriaMessage[PasswordCriteria.Digit]!!)
        }

        if (!input.contains(specialCharacterRegex)) {
            validationErrors.append("\n")
            validationErrors.append(criteriaMessage[PasswordCriteria.SpecialCharacter]!!)
        }

        return if (validationErrors.isEmpty()) {
            ValidationResult(isValid = true)
        } else {
            ValidationResult(isValid = false, validationError = validationErrors.toString())
        }
    }
}