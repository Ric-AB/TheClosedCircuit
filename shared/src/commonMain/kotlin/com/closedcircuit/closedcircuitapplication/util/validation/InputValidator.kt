package com.closedcircuit.closedcircuitapplication.util.validation

interface InputValidator {
    fun validate(input: String): ValidationResult
}