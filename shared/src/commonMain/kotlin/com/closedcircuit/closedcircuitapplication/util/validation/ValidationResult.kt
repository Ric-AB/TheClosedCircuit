package com.closedcircuit.closedcircuitapplication.util.validation

data class ValidationResult(
    val isValid: Boolean,
    val validationError: String = ""
)
