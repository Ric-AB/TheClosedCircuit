package com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiNotification(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String?,
    val message: String,
    @SerialName("is_donation")
    val isDonation: Boolean?,
    @SerialName("is_read")
    val isRead: Boolean,
    val avatar: String,
    @SerialName("fullname")
    val fullName: String,
    @SerialName("business_name")
    val businessName: String,
    @SerialName("amount_offered")
    val amountOffered: Double,
    @SerialName("user")
    val userID: String
)
