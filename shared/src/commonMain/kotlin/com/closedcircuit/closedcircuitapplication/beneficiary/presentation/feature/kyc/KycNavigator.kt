package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


internal class KycNavigator : Screen, KoinComponent {
    private val userRepository: UserRepository by inject()

    @Composable
    override fun Content() {
        ScreenContent()
    }

    @Composable
    private fun ScreenContent() {
        var innerNavigator: Navigator? by remember { mutableStateOf(null) }
        val startScreen = remember {
            val user = userRepository.userFlow.value
            if (user?.hasAttemptedKyc == true) KycStatusScreen()
            else KycHomeScreen()
        }

        Column {
            Navigator(startScreen) {
                innerNavigator = it
                ScreenBasedTransition(
                    navigator = it,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
