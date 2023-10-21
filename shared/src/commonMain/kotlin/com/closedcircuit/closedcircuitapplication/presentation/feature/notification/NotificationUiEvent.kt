package com.closedcircuit.closedcircuitapplication.presentation.feature.notification

import com.closedcircuit.closedcircuitapplication.domain.model.ID

sealed interface NotificationUiEvent {
    object MarkAllAsRead : NotificationUiEvent
    data class DeleteNotification(val index: Int, val id: ID) : NotificationUiEvent
    data class ToggleSelection(val index: Int) : NotificationUiEvent
    object ResetSelection : NotificationUiEvent
    object DeleteMultipleNotifications : NotificationUiEvent
}