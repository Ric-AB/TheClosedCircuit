package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.unit.dp
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
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.aboutus.AboutUsScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationlist.ChatTab
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification.NotificationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home.ProfileTab
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification.ProfileVerificationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings.SettingsScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.BottomNavFab
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.NavigationDrawer
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootEvent
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootResult
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.TabNavigationItem
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
internal class BeneficiaryBottomTabs : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = findRootNavigator(navigator)
        val viewModel = navigator.getNavigatorScreenModel<RootViewModel>()
        val rootState = viewModel.state.collectAsState().value
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val handleDrawerAction: (() -> Unit) -> Unit = { extraAction ->
            scope.launch {
                drawerState.close()
                extraAction.invoke()
            }
        }

        val navigateToNotificationScreen: () -> Unit = {
            handleDrawerAction { navigator.push(NotificationScreen()) }
        }

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                RootResult.LogoutSuccess -> rootNavigator.replaceAll(LoginScreen())
            }
        }



        NavigationDrawer(
            drawerState = drawerState,
            profileUrl = rootState?.profileUrl ?: "",
            fullName = rootState?.fullName ?: "__",
            activeProfile = rootState?.activeProfile?.displayText ?: "",
            navigateToNotifications = navigateToNotificationScreen,
            navigateToSettings = {
                handleDrawerAction { navigator.push(SettingsScreen()) }
            },
            navigateToAboutUs = {
                handleDrawerAction { navigator.push(AboutUsScreen()) }
            },
            logout = {
                handleDrawerAction { viewModel.onEvent(RootEvent.Logout) }
            }
        ) {
            TabNavigator(tab = DashboardTab) {
                Scaffold(
                    contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                    topBar = {
                        BeneficiaryTopAppBar(
                            openDrawer = { scope.launch { drawerState.open() } },
                            navigateToNotificationScreen = navigateToNotificationScreen
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
                            TabNavigationItem(ProfileTab)
                            BottomNavFab(
                                imageResource = SharedRes.images.ic_four_squares,
                                onClick = {
                                    if (rootState?.isEmailVerified == true) {
                                        navigator.push(PlanListScreen())
                                    } else {
                                        rootState?.email?.let {
                                            navigator.push(ProfileVerificationScreen(it))
                                        }
                                    }
                                }
                            )
                            TabNavigationItem(ChatTab)
                            TabNavigationItem(AccountTab)
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun BeneficiaryTopAppBar(
        openDrawer: () -> Unit,
        navigateToNotificationScreen: () -> Unit
    ) {
        if (LocalTabNavigator.current.current == DashboardTab)
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
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