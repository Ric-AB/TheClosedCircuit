package com.closedcircuit.closedcircuitapplication.common.domain.model

enum class StepStatus(val displayText: String, val requestValue: String) {
    NOT_COMPLETED("Not completed", "not_completed"),
    AWAITING_APPROVAL("Awaiting approval", "awaiting_approval"),
    COMPLETED("Completed", "completed");

    companion object {
        val map = values().associateBy { it.requestValue }
        fun fromText(text: String) =
            map[text] ?: throw RuntimeException("No matching value for $text")
    }
}