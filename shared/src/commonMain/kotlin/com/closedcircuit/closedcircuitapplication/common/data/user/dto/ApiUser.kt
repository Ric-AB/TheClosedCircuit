package com.closedcircuit.closedcircuitapplication.common.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiUser(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("fullname")
    val fullName: String,
    @SerialName("preferred_name")
    val preferredName: String?,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("country")
    val country: String,
    @SerialName("is_verified")
    val isVerified: Boolean,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("kyc_status")
    val kycStatus: String,
    @SerialName("phone_number_verified")
    val phoneNumberVerified: String,
    @SerialName("card_token")
    val cardToken: String?,
    @SerialName("currency")
    val currency: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)
