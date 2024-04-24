package com.closedcircuit.closedcircuitapplication.common.util.validation

interface InputValidator {
    fun validate(input: String): ValidationResult
}