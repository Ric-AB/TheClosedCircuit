package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.SplashScreen
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
internal fun AppNavigation() {
    Navigator(SplashScreen()) {
        ScreenBasedTransition(it)
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
                Icon(
                    painter = it,
                    contentDescription = tab.options.title,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
fun BottomNavFab(
    modifier: Modifier = Modifier,
    imageResource: ImageResource,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
    ) {
        Icon(painter = painterResource(imageResource), contentDescription = null)
    }
}

//@Composable
//internal fun rememberAuthState(
//    scope: CoroutineScope = rememberCoroutineScope(),
//): AuthState = remember { AuthState(scope = scope) }
//
//
//internal class AuthState(scope: CoroutineScope) : KoinComponent {
//
//    private val appSettingsRepository: AppSettingsRepository by inject()
//    var authState by mutableStateOf<AuthenticationState?>(value = null)
//        private set
//
//    init {
//        scope.launch {
//            appSettingsRepository.onboardingStateFlow().collectLatest {
//                authState = if (it) AuthenticationState.FIRST_TIME
//                else AuthenticationState.LOGGED_OUT
//            }
//        }
//    }
//}