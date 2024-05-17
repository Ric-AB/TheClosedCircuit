package com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification.NotificationRepository
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
    private val notificationItems = mutableStateListOf<NotificationItem>()


    val state: StateFlow<NotificationUIState> =
        combine(
            initialLoadingFlow,
            errorLoadingMessageFlow,
            isLoadingFlow,
            snapshotFlow { notificationItems }
        ) { initialLoading, errorLoadingMessage, isLoading, notifications ->
            when {
                initialLoading -> NotificationUIState.InitialLoading
                errorLoadingMessage != null -> NotificationUIState.Error(errorLoadingMessage)
                else -> {
                    NotificationUIState.Content(
                        isLoading = isLoading,
                        notificationItems = notifications
                    )
                }
            }
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NotificationUIState.InitialLoading
        )

    init {
        getNotifications()
    }

    fun onEvent(event: NotificationUiEvent) {
        when (event) {
            NotificationUiEvent.DeleteMultipleNotifications -> deleteMultipleNotifications()
            is NotificationUiEvent.DeleteNotification ->
                deleteNotification(index = event.index, id = event.id)

            NotificationUiEvent.MarkAllAsRead -> markAllAsRead()
            is NotificationUiEvent.ToggleSelection -> toggleSelection(event.index)
            NotificationUiEvent.ResetSelection -> resetSelection()
        }
    }

    private fun getNotifications() {
        initialLoadingFlow.update { true }
        screenModelScope.launch {
            notificationRepository.getNotifications().onSuccess { notifications ->
                val notificationsState = notifications.map {
                    NotificationItem(isSelected = false, notification = it)
                }.toMutableStateList()

                this@NotificationViewModel.notificationItems.clear()
                this@NotificationViewModel.notificationItems.addAll(notificationsState)
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

        val partitionedNotifications = notificationItems.partition { it.isSelected }
        val selectedIds = partitionedNotifications.first.map { it.notification.id }

        screenModelScope.launch {
            notificationRepository.deleteMultipleNotifications(selectedIds)
                .onSuccess {
                    isLoadingFlow.update { false }
                    notificationItems.removeAll(partitionedNotifications.first)
                }.onError { _, message ->
                    isLoadingFlow.update { false }
                    errorLoadingMessageFlow.emit(message)
                }
        }
    }

    private fun deleteNotification(index: Int, id: ID) {
        isLoadingFlow.update { true }

        screenModelScope.launch {
            notificationRepository.deleteNotification(id)
                .onSuccess {
                    isLoadingFlow.update { false }
                    notificationItems.removeAt(index)
                }.onError { _, message ->
                    isLoadingFlow.update { false }
                    errorLoadingMessageFlow.emit(message)
                }
        }
    }

    private fun markAllAsRead() {
        screenModelScope.launch {
            notificationRepository.markAllAsRead()
        }
    }

    private fun toggleSelection(index: Int) {
        val item = notificationItems[index]
        val isItemSelected = item.isSelected

        notificationItems[index] = item.copy(isSelected = !isItemSelected)
    }

    private fun resetSelection() {
        for (i in 0 until notificationItems.size) {
            val item = notificationItems[i]
            notificationItems[i] = item.copy(isSelected = false)
        }
    }
}