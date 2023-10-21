package com.closedcircuit.closedcircuitapplication.presentation.feature.account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.presentation.feature.account.home.AccountDashboardScreen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource

internal object AccountNavigator : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(SharedRes.strings.account)
            val icon = rememberVectorPainter(Icons.Outlined.AccountBox)

            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(AccountDashboardScreen()) {
            ScreenBasedTransition(it)
        }
    }
}