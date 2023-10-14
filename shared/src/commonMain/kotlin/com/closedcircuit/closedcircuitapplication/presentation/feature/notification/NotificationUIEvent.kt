package com.closedcircuit.closedcircuitapplication.presentation.feature.notification

import com.closedcircuit.closedcircuitapplication.domain.model.ID

sealed interface NotificationUIEvent {
    object MarkAllAsRead : NotificationUIEvent
    data class DeleteNotification(val index: Int, val id: ID) : NotificationUIEvent
    data class ToggleSelection(val index: Int) : NotificationUIEvent
    object ResetSelection : NotificationUIEvent
    object DeleteMultipleNotifications : NotificationUIEvent
}