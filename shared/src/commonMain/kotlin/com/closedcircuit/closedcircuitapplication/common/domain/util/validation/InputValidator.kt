package com.closedcircuit.closedcircuitapplication.common.domain.util.validation

interface InputValidator {
    fun validate(input: String): ValidationResult
}