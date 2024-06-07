package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding.OnboardingScreen
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.BeneficiaryBottomTabs
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.SponsorBottomTabs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Composable
internal fun AppNavigation() {
    Navigator(SplashScreen) {
        ScreenBasedTransition(it)
    }
}

@Composable
internal fun rememberAuthState(
    scope: CoroutineScope = rememberCoroutineScope(),
): AuthState = remember { AuthState(scope = scope) }


internal class AuthState(scope: CoroutineScope) : KoinComponent {

    private val appSettingsRepository: AppSettingsRepository by inject()
    var authState by mutableStateOf<AuthenticationState?>(value = null)
        private set

    init {
        scope.launch {
            appSettingsRepository.onboardingStateFlow().collectLatest {
                authState = if (it) AuthenticationState.FIRST_TIME
                else AuthenticationState.LOGGED_OUT
            }
        }
    }
}

enum class AuthenticationState {
    LOGGED_IN, LOGGED_OUT, FIRST_TIME
}

object SplashScreen : Screen, KoinComponent {
    private val isLoggedInUseCase by inject<IsLoggedInUseCase>()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            when (isLoggedInUseCase()) {
                AuthenticationState.LOGGED_IN -> navigator.replace(SponsorBottomTabs())
                AuthenticationState.LOGGED_OUT -> navigator.replace(LoginScreen())
                AuthenticationState.FIRST_TIME -> navigator.replace(OnboardingScreen)
            }
        }

        Spacer(Modifier.fillMaxSize())
    }
}

fun findRootNavigator(navigator: Navigator): Navigator {
    return if (navigator.parent == null) navigator
    else findRootNavigator(navigator.parent!!)
}

@Composable
fun RowScope.TabNavigationItem(tab: Tab) {
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