package com.closedcircuit.closedcircuitapplication.common.domain.model

enum class ProfileType(val displayText: String, val value: String) {
    BENEFICIARY("Beneficiary", "beneficiary"),
    SPONSOR("Sponsor", "sponsor");

    fun getPlural() = when (this) {
        BENEFICIARY -> "Beneficiaries"
        SPONSOR -> "Sponsors"
    }
}