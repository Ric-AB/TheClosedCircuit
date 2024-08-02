package com.closedcircuit.closedcircuitapplication.common.domain.model

import com.closedcircuit.closedcircuitapplication.common.domain.country.Country
import kotlinx.serialization.Serializable

@Serializable
class PhoneNumber(val dialCode: String, val value: String) {
    val phoneWithCode = "$dialCode$value"
    init {
        val regex = Regex(
            "^[+](?:[0-9\\-()/" +
                    ".]\\s?){6,15}[0-9]$"
        )

        require(regex.matches(phoneWithCode)) { "Invalid phone number- $phoneWithCode" }
    }

    companion object {
        fun fromPhoneWithCode(country: Country, phoneWithCode: String): PhoneNumber {
            val dialCode = country.dialCode
            if (!phoneWithCode.startsWith(dialCode)) throw Exception("Country dial code must match phone number code")

            val strippedPhoneNumber = phoneWithCode.drop(dialCode.length)
            return PhoneNumber(dialCode, strippedPhoneNumber)
        }
    }
}