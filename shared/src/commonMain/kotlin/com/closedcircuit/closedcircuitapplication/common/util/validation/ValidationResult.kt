package com.closedcircuit.closedcircuitapplication.common.util.validation

data class ValidationResult(
    val isValid: Boolean,
    val validationError: String = ""
)
