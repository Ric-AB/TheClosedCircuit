package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.BeneficiaryBottomTabs
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.domain.model.orDefault
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalCurrency
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ScreenKey.PROTECTED_NAVIGATOR_SCREEN
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.SponsorBottomTabs
import org.koin.core.component.KoinComponent
import kotlin.random.Random


internal class ProtectedNavigator(private val activeProfile: ProfileType) : Screen,
    KoinComponent {
    override val key: ScreenKey =
        super.key + "$PROTECTED_NAVIGATOR_SCREEN${
            Random.nextDouble(
                Double.MIN_VALUE,
                Double.MAX_VALUE
            )
        }"

    @Composable
    override fun Content() {
        val initialScreen = if (activeProfile == ProfileType.BENEFICIARY) BeneficiaryBottomTabs()
        else SponsorBottomTabs()

        val viewModel = LocalNavigator.currentOrThrow.getNavigatorScreenModel<RootViewModel>()
        val rootState = viewModel.state.collectAsState().value
        val currency = remember(rootState?.currency) { rootState?.currency.orDefault() }

        CompositionLocalProvider(LocalCurrency provides currency) {
            Navigator(initialScreen) { navigator ->
                ScreenBasedTransition(navigator)
            }
        }
    }
}