package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.message.MessageTab
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.ProfileNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.BottomNavFab
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.TabNavigationItem
import com.closedcircuit.closedcircuitapplication.common.presentation.util.justPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.account.SponsorAccountTab
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard.SponsorDashboardTab

class SponsorBottomTabs : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        TabNavigator(tab = SponsorDashboardTab) {
            Scaffold(
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