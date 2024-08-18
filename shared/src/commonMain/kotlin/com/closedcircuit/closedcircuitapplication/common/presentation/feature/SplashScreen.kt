package com.closedcircuit.closedcircuitapplication.common.presentation.feature

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding.OnboardingScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ProtectedNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.MakeOfferNavigator
import dev.theolm.rinku.compose.ext.DeepLinkListener
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
        var planId by remember { mutableStateOf<String?>(null) }

        DeepLinkListener {
            planId = it.data.substringAfterLast("/")
        }

        LaunchedEffect(rootState, planId) {
            if (rootState != null) {
                val isLoggedIn = rootState.authState == AuthenticationState.LOGGED_IN
                println("PLANID:: $planId")
                when {
//                    planId != null -> {
//                        navigator.replace(
//                            MakeOfferNavigator(
//                                planID = ID(planId!!),
//                                isLoggedIn = isLoggedIn
//                            )
//                        )
//                    }

                    isLoggedIn -> navigator.replace(ProtectedNavigator(rootState.activeProfile))
                    rootState.authState == AuthenticationState.LOGGED_OUT ->
                        navigator.replace(LoginScreen())

                    rootState.authState == AuthenticationState.FIRST_TIME ->
                        navigator.replace(OnboardingScreen)
                }
            }
        }

        remember(planId) {
            if (planId != null) {
                val isLoggedIn = rootState?.authState == AuthenticationState.LOGGED_IN
                navigator.replace(
                    MakeOfferNavigator(
                        planID = ID(planId!!),
                        isLoggedIn = isLoggedIn
                    )
                )
            }
        }

        Spacer(Modifier.fillMaxSize())
    }
}