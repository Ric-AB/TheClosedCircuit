package com.closedcircuit.closedcircuitapplication.core.validation

interface InputValidator {
    fun validate(input: String): ValidationResult
}