package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.BeneficiaryBottomTabs
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.SplashScreen
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.SponsorBottomTabs
import org.koin.core.component.KoinComponent
import kotlin.random.Random


internal class ProtectedNavigator : Screen, KoinComponent {
    override val key: ScreenKey =
        super.key + "${Random.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)}"

    @Composable
    override fun Content() {
        Navigator(SplashScreen()) { navigator ->
            val viewModel = navigator.getNavigatorScreenModel<RootViewModel>()
            viewModel.activeProfile.observeWithScreen {
                if (it == ProfileType.BENEFICIARY) {
                    navigator.replaceAll(BeneficiaryBottomTabs())
                } else {
                    navigator.replaceAll(SponsorBottomTabs())
                }
            }

            ScreenBasedTransition(navigator)
        }
    }
}