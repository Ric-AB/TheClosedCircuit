package com.closedcircuit.closedcircuitapplication.beneficiary.domain.user

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.AccountType
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Country
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Email
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: ID,
    val email: Email,
    val fullName: Name,
    val preferredName: Name?,
    val avatar: Avatar,
    val isVerified: Boolean,
    val phoneNumber: PhoneNumber,
    val country: Country,
    val kycStatus: KycStatus,
    val phoneNumberStatus: KycStatus,
    val currency: Currency?,
    val isCardTokenized: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
) {
    val firstName = Name(fullName.value.split(Regex("\\s")).first())
    val lastName = Name(fullName.value.split(Regex("\\s")).last())
    val hasAttemptedKyc get() = kycStatus != KycStatus.NOT_STARTED
    val accountType get() = AccountType.getByText(country.value)

}
