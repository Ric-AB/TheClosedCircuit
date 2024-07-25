package com.closedcircuit.closedcircuitapplication.common.data.notification

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.dto.DeleteMultipleNotificationRequest
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification.NotificationRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification.Notifications
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NotificationRepositoryImpl(
    private val notificationService: NotificationService,
    private val ioDispatcher: CoroutineDispatcher
) : NotificationRepository {

    override suspend fun getNotifications(): ApiResponse<Notifications> {
        return withContext(ioDispatcher) {
            notificationService.getNotifications().mapOnSuccess { response ->
                response.notifications.asNotifications()
            }
        }
    }

    override suspend fun markAllAsRead(): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            notificationService.markAllAsRead()
        }
    }

    override suspend fun deleteNotification(id: ID): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            notificationService.deleteNotification(id.value)
        }
    }

    override suspend fun deleteMultipleNotifications(ids: List<ID>): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            val body = DeleteMultipleNotificationRequest(notifications = ids.map { it.value })
            notificationService.deleteMultipleNotifications(body)
        }
    }
}