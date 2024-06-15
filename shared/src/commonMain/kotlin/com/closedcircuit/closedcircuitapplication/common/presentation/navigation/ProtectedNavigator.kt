package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.BeneficiaryBottomTabs
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.SponsorBottomTabs
import org.koin.core.component.KoinComponent
import kotlin.random.Random


internal class ProtectedNavigator(private val activeProfile: ProfileType) : Screen,
    KoinComponent {
    override val key: ScreenKey =
        super.key + "${Random.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)}"

    @Composable
    override fun Content() {
        val initialScreen = if (activeProfile == ProfileType.BENEFICIARY) BeneficiaryBottomTabs()
        else SponsorBottomTabs()

        Navigator(initialScreen) { navigator ->
            ScreenBasedTransition(navigator)
        }
    }
}