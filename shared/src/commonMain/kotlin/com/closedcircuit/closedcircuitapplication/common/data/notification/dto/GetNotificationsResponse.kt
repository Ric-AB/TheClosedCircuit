package com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationsResponse(
    val notifications: List<ApiNotification>
)
