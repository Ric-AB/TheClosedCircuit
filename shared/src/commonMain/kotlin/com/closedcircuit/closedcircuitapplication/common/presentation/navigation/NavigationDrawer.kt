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
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.components.Avatar
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Stable
data class NavigationDrawerProfileState(
    val profileUrl: String,
    val fullName: String,
    val email: String
)

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    profileState: NavigationDrawerProfileState,
    logout: () -> Unit,
    content: @Composable () -> Unit
) {
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
                            imageUrl = profileState.profileUrl,
                            size = DpSize(60.dp, 60.dp)
                        )

                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = profileState.fullName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = profileState.email,
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
                        onClick = {}
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
                        onClick = {}
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
                                    contentDescription = "about us",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            label = { Text(stringResource(SharedRes.strings.logout_label)) },
                            selected = false,
                            onClick = logout,
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                }
            }
        },
        content = content
    )
}