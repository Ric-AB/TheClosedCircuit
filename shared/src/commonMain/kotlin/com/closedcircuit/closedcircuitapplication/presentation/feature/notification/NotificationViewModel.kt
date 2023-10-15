package com.closedcircuit.closedcircuitapplication.presentation.feature.notification

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ScreenModel {

    private val initialLoadingFlow = MutableStateFlow(true)
    private val errorLoadingMessageFlow = MutableStateFlow<String?>(null)
    private val isLoadingFlow = MutableStateFlow(false)
    private val notificationsState = mutableStateListOf<NotificationState>()


    val state: StateFlow<NotificationUIState> =
        combine(
            initialLoadingFlow,
            errorLoadingMessageFlow,
            isLoadingFlow,
            snapshotFlow { notificationsState }
        ) { initialLoading, errorLoadingMessage, isLoading, notifications ->
            when {
                initialLoading -> NotificationUIState.InitialLoading
                errorLoadingMessage != null -> NotificationUIState.Error(errorLoadingMessage)
                else -> {
                    NotificationUIState.DataLoaded(
                        isLoading = isLoading,
                        notificationsState = notifications
                    )
                }
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NotificationUIState.InitialLoading
        )

    init {
        getNotifications()
    }

    fun onEvent(event: NotificationUIEvent) {
        when (event) {
            NotificationUIEvent.DeleteMultipleNotifications -> deleteMultipleNotifications()
            is NotificationUIEvent.DeleteNotification ->
                deleteNotification(index = event.index, id = event.id)

            NotificationUIEvent.MarkAllAsRead -> markAllAsRead()
            is NotificationUIEvent.ToggleSelection -> toggleSelection(event.index)
            NotificationUIEvent.ResetSelection -> resetSelection()
        }
    }

    private fun getNotifications() {
        initialLoadingFlow.update { true }
        coroutineScope.launch {
            notificationRepository.getNotifications().onSuccess { notifications ->
                val notificationsState = notifications.map {
                    NotificationState(isSelected = false, notification = it)
                }.toMutableStateList()

                this@NotificationViewModel.notificationsState.clear()
                this@NotificationViewModel.notificationsState.addAll(notificationsState)
                errorLoadingMessageFlow.update { null }
                initialLoadingFlow.update { false }
            }.onError { _, message ->
                initialLoadingFlow.update { false }
                errorLoadingMessageFlow.update { message }
            }
        }
    }

    private fun deleteMultipleNotifications() {
        isLoadingFlow.update { true }

        val partitionedNotifications = notificationsState.partition { it.isSelected }
        val selectedIds = partitionedNotifications.first.map { it.notification.id }

        coroutineScope.launch {
            notificationRepository.deleteMultipleNotifications(selectedIds)
                .onSuccess {
                    isLoadingFlow.update { false }
                    notificationsState.removeAll(partitionedNotifications.first)
                }
        }
    }

    private fun deleteNotification(index: Int, id: ID) {
        isLoadingFlow.update { true }

        coroutineScope.launch {
            notificationRepository.deleteNotification(id)
                .onSuccess {
                    isLoadingFlow.update { false }
                    notificationsState.removeAt(index)
                }
        }
    }

    private fun markAllAsRead() {
        coroutineScope.launch {
            notificationRepository.markAllAsRead()
        }
    }

    private fun toggleSelection(index: Int) {
        val item = notificationsState[index]
        val isItemSelected = item.isSelected

        notificationsState[index] = item.copy(isSelected = !isItemSelected)
    }

    private fun resetSelection() {
        for (i in 0 until notificationsState.size) {
            val item = notificationsState[i]
            notificationsState[i] = item.copy(isSelected = false)
        }
    }
}