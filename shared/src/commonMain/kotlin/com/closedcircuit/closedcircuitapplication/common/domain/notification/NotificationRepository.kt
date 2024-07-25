package com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.notification.Notification
import kotlinx.collections.immutable.ImmutableList

typealias Notifications = ImmutableList<Notification>

interface NotificationRepository {

    suspend fun getNotifications(): ApiResponse<Notifications>
    suspend fun markAllAsRead(): ApiResponse<Unit>
    suspend fun deleteNotification(id: ID): ApiResponse<Unit>
    suspend fun deleteMultipleNotifications(ids: List<ID>): ApiResponse<Unit>
}