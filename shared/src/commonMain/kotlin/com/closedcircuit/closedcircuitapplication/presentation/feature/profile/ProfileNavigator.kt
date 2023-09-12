package com.closedcircuit.closedcircuitapplication.presentation.feature.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home.ProfileScreen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

internal object ProfileNavigator : Tab, KoinComponent {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(SharedRes.strings.profile)
            val icon = rememberVectorPainter(Icons.Outlined.Person)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(ProfileScreen) {
            ScreenBasedTransition(it)
        }
    }
}
