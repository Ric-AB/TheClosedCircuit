package com.closedcircuit.closedcircuitapplication.core.validation

data class ValidationResult(
    val isValid: Boolean,
    val validationErrors: List<String> = emptyList()
)
