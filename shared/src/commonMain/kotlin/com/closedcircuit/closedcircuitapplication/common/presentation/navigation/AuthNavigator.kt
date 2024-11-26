package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding.OnboardingScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ScreenKeys.AUTH_NAVIGATOR
import dev.theolm.rinku.compose.ext.DeepLinkListener
import org.koin.core.component.KoinComponent
import kotlin.random.Random

internal class AuthNavigator(private val isFirstTime: Boolean) : Screen, KoinComponent {

    override val key: ScreenKey =
        super.key + "$AUTH_NAVIGATOR${
            Random.nextDouble(
                Double.MIN_VALUE,
                Double.MAX_VALUE
            )
        }"

    @Composable
    override fun Content() {
        val initialScreen = if (isFirstTime) OnboardingScreen
        else LoginScreen()

        var planId by remember { mutableStateOf<String?>(null) }
        DeepLinkListener {
            planId = it.data.substringAfterLast("/")
        }

        Navigator(initialScreen, key = AUTH_NAVIGATOR) { navigator ->
            val rootNavigator = findRootNavigator(navigator)
            ScreenBasedTransition(navigator)
            planDeepLinkHandler(planId, rootNavigator)
        }
    }
}