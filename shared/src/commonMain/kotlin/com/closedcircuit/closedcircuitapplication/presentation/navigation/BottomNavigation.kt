package com.closedcircuit.closedcircuitapplication.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.closedcircuit.closedcircuitapplication.presentation.feature.account.AccountTab
import com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard.DashboardTab
import com.closedcircuit.closedcircuitapplication.presentation.feature.message.MessageTab
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.ProfileNavigator
import com.closedcircuit.closedcircuitapplication.presentation.util.justPadding

internal object BottomNavigation : Screen {

    @Composable
    override fun Content() {
        TabNavigator(tab = DashboardTab) {
            Scaffold(
                content = {
                    Column(modifier = Modifier.justPadding(bottom = it.calculateBottomPadding())) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(DashboardTab)
                        TabNavigationItem(ProfileNavigator)
                        TabNavigationItem(MessageTab)
                        TabNavigationItem(AccountTab)
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(painter = it, contentDescription = tab.options.title)
            }
        }
    )
}