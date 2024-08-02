package com.closedcircuit.closedcircuitapplication.common.data.user

import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.ApiUser
import com.closedcircuit.closedcircuitapplication.common.domain.country.Country
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.common.domain.user.User

fun ApiUser.toUser(country: Country) = User(
    id = ID(id),
    email = Email(email),
    fullName = Name(fullName),
    preferredName = preferredName?.let { Name(it) },
    avatar = ImageUrl(avatar),
    isVerified = isVerified,
    phoneNumber = PhoneNumber.fromPhoneWithCode(country, phoneNumber),
    country = country,
    kycStatus = KycStatus.valueOf(kycStatus),
    phoneNumberStatus = KycStatus.valueOf(phoneNumberVerified),
    currency = currency?.let { Currency(it) },
    isCardTokenized = !cardToken.isNullOrBlank(),
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun User.toApiUser() = ApiUser(
    id = id.value,
    email = email.value,
    fullName = fullName.value,
    preferredName = preferredName?.value,
    avatar = avatar.value,
    country = country.name,
    isVerified = isVerified,
    phoneNumber = phoneNumber.phoneWithCode,
    kycStatus = kycStatus.toString(),
    phoneNumberVerified = phoneNumberStatus.toString(),
    cardToken = null,
    currency = currency?.value,
    createdAt = createdAt.value,
    updatedAt = updatedAt.value
)