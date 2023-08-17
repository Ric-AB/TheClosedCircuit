package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.data.user.dto.UserResponse
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.value.Country
import com.closedcircuit.closedcircuitapplication.domain.value.Currency
import com.closedcircuit.closedcircuitapplication.domain.value.Email
import com.closedcircuit.closedcircuitapplication.domain.value.ID
import com.closedcircuit.closedcircuitapplication.domain.value.Name
import com.closedcircuit.closedcircuitapplication.domain.value.PhoneNumber
import com.closedcircuit.closedcircuitapplication.domain.value.RawDate
import com.closedcircuit.closedcircuitapplication.domain.value.Avatar
import com.closedcircuit.closedcircuitapplication.domain.value.VerificationStatus

fun UserResponse.toUser() = User(
    id = ID(id),
    email = Email(email),
    fullName = Name(fullName),
    preferredName = preferredName?.let { Name(it) },
    avatar = Avatar(avatar),
    isVerified = isVerified,
    phoneNumber = PhoneNumber(phoneNumber),
    country = Country(country),
    kycStatus = VerificationStatus.valueOf(kycStatus),
    phoneNumberStatus = VerificationStatus.valueOf(phoneNumberVerified),
    currency = currency?.let { Currency(it) },
    isCardTokenized = !cardToken.isNullOrBlank(),
    createdAt = RawDate(createdAt),
    updatedAt = RawDate(updatedAt)
)
