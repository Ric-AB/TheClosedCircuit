package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.data.user.dto.SponsorResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserResponse
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.model.Country
import com.closedcircuit.closedcircuitapplication.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.domain.model.Email
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.VerificationStatus
import com.closedcircuit.closedcircuitapplication.domain.user.Sponsor

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
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun SponsorResponse.toSponsor() = Sponsor(
    avatar = Avatar(sponsorAvatar),
    fullName = Name(sponsorFullName),
    loanAmount = Price(loanAmount)
)
