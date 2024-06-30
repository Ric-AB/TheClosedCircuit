package com.closedcircuit.closedcircuitapplication.common.presentation.feature

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding.OnboardingScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ProtectedNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class SplashScreen : Screen, KoinComponent {
    override val key: ScreenKey =
        super.key + "${Random.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)}"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<RootViewModel>()
        val rootState = viewModel.state.collectAsState().value

        LaunchedEffect(rootState) {
            println("###:: $rootState")
            if (rootState != null) {
                when (rootState.authState) {
                    AuthenticationState.LOGGED_IN -> navigator.replace(ProtectedNavigator(rootState.activeProfile))
                    AuthenticationState.LOGGED_OUT -> navigator.replace(LoginScreen())
                    AuthenticationState.FIRST_TIME -> navigator.replace(OnboardingScreen)
                }
            }
        }

        Spacer(Modifier.fillMaxSize())
    }
}