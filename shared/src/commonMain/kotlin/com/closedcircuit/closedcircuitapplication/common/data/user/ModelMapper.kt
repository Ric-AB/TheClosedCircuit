package com.closedcircuit.closedcircuitapplication.beneficiary.data.user

import com.closedcircuit.closedcircuitapplication.common.data.user.dto.SponsorResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.ApiUser
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.User
import com.closedcircuit.closedcircuitapplication.common.domain.model.Country
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor

fun ApiUser.asUser() = User(
    id = ID(id),
    email = Email(email),
    fullName = Name(fullName),
    preferredName = preferredName?.let { Name(it) },
    avatar = ImageUrl(avatar),
    isVerified = isVerified,
    phoneNumber = PhoneNumber(phoneNumber),
    country = Country(country),
    kycStatus = KycStatus.valueOf(kycStatus),
    phoneNumberStatus = KycStatus.valueOf(phoneNumberVerified),
    currency = currency?.let { Currency(it) },
    isCardTokenized = !cardToken.isNullOrBlank(),
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun User.asApiUser() = ApiUser(
    id = id.value,
    email = email.value,
    fullName = fullName.value,
    preferredName = preferredName?.value,
    avatar = avatar.value,
    country = country.value,
    isVerified = isVerified,
    phoneNumber = phoneNumber.value,
    kycStatus = kycStatus.toString(),
    phoneNumberVerified = phoneNumberStatus.toString(),
    cardToken = null,
    currency = currency?.value,
    createdAt = createdAt.value,
    updatedAt = updatedAt.value
)

fun SponsorResponse.toSponsor() = Sponsor(
    avatar = ImageUrl(sponsorAvatar),
    fullName = Name(sponsorFullName),
    loanAmount = Amount(loanAmount.toDouble())
)
