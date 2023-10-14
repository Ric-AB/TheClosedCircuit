@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.notification

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.CircularIndicator
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.presentation.util.conditional
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object NotificationScreen : Screen, KoinComponent {
    private val viewModel: NotificationViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = viewModel.state.collectAsState().value
        val onEvent = viewModel::onEvent

        ScreenContent(
            uiState = uiState,
            goBack = navigator::pop,
            toggleSelection = { onEvent(NotificationUIEvent.ToggleSelection(it)) },
            resetSelection = { onEvent(NotificationUIEvent.ResetSelection) },
            markAllAsRead = { onEvent(NotificationUIEvent.MarkAllAsRead) },
            deleteNotification = { index, id ->
                onEvent(NotificationUIEvent.DeleteNotification(index, id))
            },
            deleteMultipleNotifications = { onEvent(NotificationUIEvent.DeleteMultipleNotifications) }
        )

    }
}

@Composable
private fun ScreenContent(
    uiState: NotificationUIState,
    goBack: () -> Unit,
    toggleSelection: (Int) -> Unit,
    resetSelection: () -> Unit,
    markAllAsRead: () -> Unit,
    deleteNotification: (Int, ID) -> Unit,
    deleteMultipleNotifications: () -> Unit
) {
    var anyUnreadNotification by remember { mutableStateOf(false) }
    var isInSelectionMode by remember { mutableStateOf(false) }
    var numberOfSelectedItems by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    BaseScaffold(
        isLoading = isLoading,
        topBar = {
            if (isInSelectionMode) {
                SelectionModeTopAppBar(
                    numberOfSelectedItems = numberOfSelectedItems,
                    onCloseIconClick = resetSelection,
                    onDeleteIconClick = deleteMultipleNotifications
                )
            } else {
                NotificationTopAppBar(
                    anyUnreadNotification = anyUnreadNotification,
                    onNavigationIconClick = goBack,
                    onMarkAsReadMenuClick = markAllAsRead
                )
            }
        }
    ) { innerPadding ->
        AnimatedContent(targetState = uiState) { targetUiState ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {

                when (targetUiState) {
                    is NotificationUIState.DataLoaded -> {
                        LaunchedEffect(targetUiState.isLoading) {
                            isLoading = targetUiState.isLoading
                        }

                        NotificationBody(
                            notificationsState = targetUiState.notificationsState,
                            toggleSelection = toggleSelection,
                            deleteNotification = deleteNotification,
                            updateSelectionState = { anyUnread, anySelected, numberOfSelected ->
                                anyUnreadNotification = anyUnread
                                isInSelectionMode = anySelected
                                numberOfSelectedItems = numberOfSelected
                            }
                        )
                    }

                    is NotificationUIState.Error -> {
                        Text(
                            text = "ERROR-${targetUiState.message}",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    NotificationUIState.InitialLoading -> {
                        CircularIndicator(size = 50.dp, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationBody(
    notificationsState: SnapshotStateList<NotificationState>,
    toggleSelection: (Int) -> Unit,
    deleteNotification: (Int, ID) -> Unit,
    updateSelectionState: (Boolean, Boolean, Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (notificationsState.isEmpty()) {
            EmptyNotification(modifier = Modifier.align(Alignment.BottomCenter))
        } else {
            LaunchedEffect(notificationsState.toList()) {
                var anyUnread = false
                var anySelected = false
                var numberOfSelection = 0
                notificationsState.toList().forEach {
                    if (it.isSelected) {
                        anySelected = true
                        numberOfSelection++
                    }

                    if (!it.notification.isRead) anyUnread = true
                }

                updateSelectionState(anyUnread, anySelected, numberOfSelection)
            }

            LazyColumn {
                itemsIndexed(
                    items = notificationsState,
                    key = { _, item -> item.notification.id.value }) { index, item ->
                    NotificationItem(
                        modifier = Modifier.fillMaxWidth(),
                        notificationState = item,
                        toggleSelection = { toggleSelection(index) },
                        deleteNotification = { deleteNotification(index, item.notification.id) }
                    )

                    Divider(thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    notificationState: NotificationState,
    toggleSelection: () -> Unit,
    deleteNotification: () -> Unit
) {
    val notification = notificationState.notification
    Row(
        modifier = modifier
            .conditional(
                condition = notificationState.isSelected,
                ifTrue = {
                    background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level2)
                    )
                },
                ifFalse = { background(color = MaterialTheme.colorScheme.surface) }
            ).clickable(enabled = true, onClick = toggleSelection)
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Avatar(
            avatar = com.closedcircuit.closedcircuitapplication.domain.model.Avatar(""),
            size = DpSize(50.dp, 50.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.createdAt.value,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.width(12.dp))
        NotificationItemDropDownMenu(onDeleteMenuClick = deleteNotification)
    }
}

@Composable
private fun EmptyNotification(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(SharedRes.images.no_notification_illustration),
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(SharedRes.strings.no_notifications),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(SharedRes.strings.no_notifications_prompt),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NotificationItemDropDownMenu(onDeleteMenuClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = { expanded = false; onDeleteMenuClick() }
            )
        }
    }
}

@Composable
private fun SelectionModeTopAppBar(
    numberOfSelectedItems: Int,
    onCloseIconClick: () -> Unit,
    onDeleteIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "$numberOfSelectedItems Selected") },
        navigationIcon = {
            IconButton(onClick = onCloseIconClick) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onDeleteIconClick) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
            }
        }
    )
}

@Composable
private fun NotificationTopAppBar(
    anyUnreadNotification: Boolean,
    onNavigationIconClick: () -> Unit,
    onMarkAsReadMenuClick: () -> Unit
) {
    TopAppBar(
        title = { TitleText(text = stringResource(SharedRes.strings.notifications)) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            if (anyUnreadNotification) {
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier.wrapContentSize(Alignment.TopEnd)
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Mark all as read") },
                            onClick = { expanded = false; onMarkAsReadMenuClick() }
                        )
                    }
                }
            }
        }
    )
}