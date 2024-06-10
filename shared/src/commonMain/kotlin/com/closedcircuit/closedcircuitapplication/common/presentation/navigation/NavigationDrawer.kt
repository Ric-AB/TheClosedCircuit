package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
//                Image(
//                    modifier = Modifier
//                        .size(250.dp)
//                        .align(Alignment.CenterHorizontally),
//                    painter = painterResource(id = com.example.ui.R.drawable.logo),
//                    contentDescription = "App Logo"
//                )
//
//                NavigationDrawerItem(
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = com.example.ui.R.drawable.google_logo),
//                            contentDescription = "Google logo",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    },
//                    label = {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text(
//                                text = stringResource(id = R.string.sign_out),
//                                modifier = Modifier.weight(1f)
//                            )
//
//                            if (signingOut) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.size(24.dp),
//                                    strokeWidth = 3.dp,
//                                )
//                            }
//                        }
//                    },
//                    selected = false,
//                    onClick = {}
//                )
//
//                NavigationDrawerItem(
//                    icon = {
//                        Icon(
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = "Delete all icon",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    },
//                    label = {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text(
//                                text = stringResource(id = R.string.delete_all_diaries),
//                                modifier = Modifier.weight(1f)
//                            )
//
//                            if (deletingDiaries) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.size(24.dp),
//                                    strokeWidth = 3.dp,
//                                )
//                            }
//                        }
//                    },
//                    selected = false,
//                    onClick = onDeleteAllClick
//                )
            }
        },
        content = content
    )
}