package com.closedcircuit.closedcircuitapplication.common.presentation.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.AuthNavigator
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
            if (rootState != null) {
                when (rootState.authState) {
                    AuthenticationState.LOGGED_IN ->
                        navigator.replace(ProtectedNavigator(rootState.activeProfile))

                    else -> navigator.replace(AuthNavigator(isFirstTime = rootState.authState == AuthenticationState.FIRST_TIME))
                }
            }
        }

        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    }
}