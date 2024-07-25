package com.closedcircuit.closedcircuitapplication.common.data.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationsResponse(
    val notifications: List<ApiNotification>
)
