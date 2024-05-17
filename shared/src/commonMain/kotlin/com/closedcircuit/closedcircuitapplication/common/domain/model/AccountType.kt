package com.closedcircuit.closedcircuitapplication.common.domain.model

enum class AccountType {
    NG, SA;

    companion object {
        fun getByText(text: String): AccountType {
            return when (text) {
                "Nigeria" -> NG
                else -> SA
            }
        }
    }
}