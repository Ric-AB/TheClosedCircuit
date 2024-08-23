package com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.closedcircuit.closedcircuitapplication.common.domain.notification.Notification


sealed interface NotificationUIState {
    object Loading : NotificationUIState

    object Empty : NotificationUIState

    data class Content(
        val isLoading: Boolean = false,
        val notificationItems: SnapshotStateList<NotificationItem> = mutableStateListOf()
    ) : NotificationUIState

    data class Error(val message: String) : NotificationUIState
}

data class NotificationItem(
    val isSelected: Boolean,
    val notification: Notification
)