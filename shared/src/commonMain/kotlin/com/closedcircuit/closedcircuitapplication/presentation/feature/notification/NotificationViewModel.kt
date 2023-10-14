package com.closedcircuit.closedcircuitapplication.presentation.feature.notification

import androidx.compose.runtime.toMutableStateList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ScreenModel {

    private val _state: MutableStateFlow<NotificationUIState> =
        MutableStateFlow(NotificationUIState.InitialLoading)
    val state: StateFlow<NotificationUIState> = _state.asStateFlow()

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
        coroutineScope.launch {
            notificationRepository.getNotifications().onSuccess { notifications ->
                val notificationsState = notifications.map {
                    NotificationState(isSelected = false, notification = it)
                }.toMutableStateList()

                _state.update {
                    NotificationUIState.DataLoaded(
                        isLoading = false,
                        notificationsState = notificationsState
                    )
                }
            }.onError { _, message ->
                _state.update { NotificationUIState.Error(message) }
            }
        }
    }

    private fun deleteMultipleNotifications() {
        _state.update { notificationUIState ->
            (notificationUIState as NotificationUIState.DataLoaded).copy(
                isLoading = true,
                notificationsState = notificationUIState.notificationsState
            )
        }

        val partitionedNotifications =
            (_state.value as NotificationUIState.DataLoaded).notificationsState
                .partition { it.isSelected }

        val selectedIds = partitionedNotifications.first.map { it.notification.id }


        coroutineScope.launch {
            notificationRepository.deleteMultipleNotifications(selectedIds)
                .onSuccess {
                    _state.update { notificationUIState ->
                        (notificationUIState as NotificationUIState.DataLoaded).copy(
                            isLoading = false,
                            notificationsState = partitionedNotifications.second.toMutableStateList()
                        )
                    }
                }
        }
    }

    private fun deleteNotification(index: Int, id: ID) {
        _state.update { notificationUIState ->
            (notificationUIState as NotificationUIState.DataLoaded).copy(
                isLoading = true,
                notificationsState = notificationUIState.notificationsState
            )
        }

        coroutineScope.launch {
            notificationRepository.deleteNotification(id)
                .onSuccess {
                    _state.update { notificationUIState ->
                        val loadedState = notificationUIState as NotificationUIState.DataLoaded
                        loadedState.notificationsState.removeAt(index)
                        loadedState.copy(
                            isLoading = false,
                            notificationsState = notificationUIState.notificationsState
                        )
                    }
                }
        }
    }

    private fun markAllAsRead() {
        coroutineScope.launch {
            notificationRepository.markAllAsRead()
        }
    }

    private fun toggleSelection(index: Int) {
        _state.update { notificationUIState ->
            val loadedState = notificationUIState as NotificationUIState.DataLoaded
            val item = loadedState.notificationsState[index]
            val isItemSelected = item.isSelected

            loadedState.notificationsState[index] = item.copy(isSelected = !isItemSelected)
            loadedState
        }
    }

    private fun resetSelection() {
        _state.update { notificationUIState ->
            val loadedState = notificationUIState as NotificationUIState.DataLoaded
            for (i in 0 until loadedState.notificationsState.size) {
                val item = loadedState.notificationsState[i]
                loadedState.notificationsState[i] = item.copy(isSelected = false)
            }

            loadedState
        }
    }
}