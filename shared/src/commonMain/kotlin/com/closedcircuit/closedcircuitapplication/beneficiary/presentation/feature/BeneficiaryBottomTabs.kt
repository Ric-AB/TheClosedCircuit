package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.account.AccountTab
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard.DashboardTab
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist.PlanListScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.message.MessageTab
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification.NotificationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.ProfileNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings.SettingsScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.BottomNavFab
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.NavigationDrawer
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.TabNavigationItem
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
internal class BeneficiaryBottomTabs : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<RootViewModel>()
        val rootState = viewModel.state.collectAsState().value
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        NavigationDrawer(
            drawerState = drawerState,
            profileUrl = rootState?.profileUrl ?: "",
            fullName = rootState?.fullName ?: "__",
            activeProfile = rootState?.activeProfile?.displayText ?: "",
            navigateToSettings = {
                scope.launch {
                    drawerState.close()
                    navigator.push(SettingsScreen())
                }
            },
            logout = { navigator.replaceAll(LoginScreen()) }
        ) {
            TabNavigator(tab = DashboardTab) {
                Scaffold(
                    topBar = {
                        BeneficiaryTopAppBar(
                            navigationIconClick = { scope.launch { drawerState.open() } },
                            navigateToNotificationScreen = { navigator.push(NotificationScreen()) }
                        )
                    },
                    content = { padding ->
                        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                            CurrentTab()
                        }
                    },
                    bottomBar = {
                        NavigationBar {
                            TabNavigationItem(DashboardTab)
                            TabNavigationItem(ProfileNavigator)
                            BottomNavFab(
                                imageResource = SharedRes.images.ic_four_squares,
                                onClick = { navigator.push(PlanListScreen()) }
                            )
                            TabNavigationItem(MessageTab)
                            TabNavigationItem(AccountTab)
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun BeneficiaryTopAppBar(
        navigationIconClick: () -> Unit,
        navigateToNotificationScreen: () -> Unit
    ) {
        if (LocalTabNavigator.current.current == DashboardTab)
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