package com.closedcircuit.closedcircuitapplication.common.domain.util.validation

data class ValidationResult(
    val isValid: Boolean,
    val validationError: String = ""
)
