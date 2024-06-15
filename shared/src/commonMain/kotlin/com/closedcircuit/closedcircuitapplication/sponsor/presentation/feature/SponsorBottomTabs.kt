package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.message.MessageTab
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification.NotificationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.ProfileNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings.SettingsScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.BottomNavFab
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.NavigationDrawer
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.NavigationDrawerProfileState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.TabNavigationItem
import com.closedcircuit.closedcircuitapplication.common.presentation.util.justPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.account.SponsorAccountTab
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard.SponsorDashboardTab
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class SponsorBottomTabs : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val profileState = remember {
            NavigationDrawerProfileState(
                profileUrl = "",
                fullName = "Richard Bajomo",
                email = "richardbajomo@gmail.com"
            )
        }
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        NavigationDrawer(
            drawerState = drawerState,
            profileState = profileState,
            navigateToSettings = {
                scope.launch {
                    drawerState.close()
                    navigator.push(SettingsScreen())
                }
            },
            logout = { navigator.replaceAll(LoginScreen()) }
        ) {
            TabNavigator(tab = SponsorDashboardTab) {
                Scaffold(
                    topBar = {
                        DashboardTopAppBar(
                            navigationIconClick = { scope.launch { drawerState.open() } },
                            navigateToNotificationScreen = { navigator.push(NotificationScreen()) }
                        )
                    },
                    content = {
                        Column(modifier = Modifier.justPadding(bottom = it.calculateBottomPadding())) {
                            CurrentTab()
                        }
                    },
                    bottomBar = {
                        NavigationBar {
                            TabNavigationItem(SponsorDashboardTab)
                            TabNavigationItem(ProfileNavigator)
                            BottomNavFab(
                                imageResource = SharedRes.images.ic_funds,
                                onClick = { }
                            )
                            TabNavigationItem(MessageTab)
                            TabNavigationItem(SponsorAccountTab)
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun DashboardTopAppBar(
        navigationIconClick: () -> Unit,
        navigateToNotificationScreen: () -> Unit
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = navigationIconClick) {
                    Icon(
                        painter = painterResource(SharedRes.images.ic_hamburger),
                        contentDescription = "hamburger menu"
                    )
                }
            },
            title = { },
            actions = {
                IconButton(onClick = navigateToNotificationScreen) {
                    Icon(
                        painter = painterResource(SharedRes.images.ic_notification),
                        contentDescription = "notifications"
                    )
                }
            }
        )
    }
}