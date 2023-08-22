package com.closedcircuit.closedcircuitapplication.core.validation

data class ValidationResult(
    val isValid: Boolean,
    val validationError: String = ""
)
