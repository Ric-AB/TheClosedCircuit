package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.domain.value.Country
import com.closedcircuit.closedcircuitapplication.domain.value.Currency
import com.closedcircuit.closedcircuitapplication.domain.value.Email
import com.closedcircuit.closedcircuitapplication.domain.value.ID
import com.closedcircuit.closedcircuitapplication.domain.value.VerificationStatus
import com.closedcircuit.closedcircuitapplication.domain.value.Name
import com.closedcircuit.closedcircuitapplication.domain.value.PhoneNumber
import com.closedcircuit.closedcircuitapplication.domain.value.RawDate
import com.closedcircuit.closedcircuitapplication.domain.value.Avatar
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
    val kycStatus: VerificationStatus,
    val phoneNumberStatus: VerificationStatus,
    val currency: Currency?,
    val isCardTokenized: Boolean,
    val createdAt: RawDate,
    val updatedAt: RawDate,
)
