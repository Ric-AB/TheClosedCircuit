package com.closedcircuit.closedcircuitapplication.domain.notification

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.collections.immutable.ImmutableList

typealias Notifications = ImmutableList<Notification>

interface NotificationRepository {

    suspend fun getNotifications(): ApiResponse<Notifications>
    suspend fun markAllAsRead(): ApiResponse<Unit>
    suspend fun deleteNotification(id: ID): ApiResponse<Unit>
    suspend fun deleteMultipleNotifications(ids: List<ID>): ApiResponse<Unit>
}