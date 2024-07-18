package com.closedcircuit.closedcircuitapplication.common.domain.model

enum class KycStatus(val displayValue: String) {
    NOT_STARTED("Not started"),
    PENDING("Pending"),
    VERIFIED("Verified"),
    FAILED("Failed")
}