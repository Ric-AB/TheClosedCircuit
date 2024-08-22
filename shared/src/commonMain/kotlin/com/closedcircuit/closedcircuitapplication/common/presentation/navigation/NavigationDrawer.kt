package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.component.AppAlertDialog
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    profileUrl: String,
    fullName: String,
    activeProfile: String,
    navigateToNotifications: () -> Unit,
    navigateToSettings: () -> Unit,
    logout: () -> Unit,
    content: @Composable () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    AppAlertDialog(
        visible = showLogoutDialog,
        onDismissRequest = { showLogoutDialog = false },
        onConfirm = logout,
        title = stringResource(SharedRes.strings.logout_label),
        text = stringResource(SharedRes.strings.logout_prompt_label),
        confirmTitle = stringResource(SharedRes.strings.yes_label),
    )

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar(
                            imageUrl = profileUrl,
                            size = DpSize(60.dp, 60.dp)
                        )

                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = fullName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = activeProfile,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()

                    Spacer(Modifier.height(40.dp))
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(SharedRes.images.ic_notification),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "notification"
                            )
                        },
                        label = { Text(stringResource(SharedRes.strings.notifications)) },
                        selected = false,
                        onClick = navigateToNotifications
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(SharedRes.images.ic_settings),
                                contentDescription = "settings",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text(stringResource(SharedRes.strings.settings_label)) },
                        selected = false,
                        onClick = navigateToSettings
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(SharedRes.images.ic_person_support),
                                contentDescription = "support",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text(stringResource(SharedRes.strings.support_label)) },
                        selected = false,
                        onClick = {}
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(SharedRes.images.ic_chat_bubble),
                                contentDescription = "about us",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        label = { Text(stringResource(SharedRes.strings.about_us_label)) },
                        selected = false,
                        onClick = {}
                    )

                    Row(modifier = Modifier.weight(1f)) {
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    painter = painterResource(SharedRes.images.ic_logout),
                                    contentDescription = "logout",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            label = { Text(stringResource(SharedRes.strings.logout_label)) },
                            selected = false,
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                }
            }
        },
        content = content
    )
}