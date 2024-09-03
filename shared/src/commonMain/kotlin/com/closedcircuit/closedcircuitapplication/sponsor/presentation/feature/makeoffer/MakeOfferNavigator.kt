package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.ProtectedNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class MakeOfferNavigator(private val planID: ID) : Screen,
    KoinComponent {
    @Composable
    override fun Content() {
        var showSuccessDialog by remember { mutableStateOf(false) }
        val messageBarState = rememberMessageBarState()
        val rootNavigator = findRootNavigator(LocalNavigator.currentOrThrow)
        val rootViewModel = rootNavigator.getNavigatorScreenModel<RootViewModel>()
        val rootState = rootViewModel.state.collectAsState().value

        Navigator(PlanSummaryScreen(planID)) { navigator ->
            BaseScaffold(
                topBar = {
                    DefaultAppBar(
                        mainAction = {
                            if (navigator.canPop) navigator.pop()
                            else rootState?.activeProfile?.let {
                                when (rootState.authState) {
                                    AuthenticationState.LOGGED_IN ->
                                        rootNavigator.replaceAll(ProtectedNavigator(it))

                                    else -> rootNavigator.replaceAll(LoginScreen())
                                }
                            }
                        }
                    )
                },
                messageBarState = messageBarState
            ) { _ ->
                ScreenBasedTransition(navigator = navigator, modifier = Modifier.fillMaxSize())
                CurrentScreen()

                val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
                viewModel.makeOfferResultChannel.receiveAsFlow().observeWithScreen { result ->
                    when (result) {
                        is MakeOfferResult.Error -> messageBarState.addError(result.message)
                        is MakeOfferResult.DonationOfferSuccess -> navigator.push(
                            PaymentScreen(
                                result.paymentLink
                            )
                        )

                        MakeOfferResult.LoanOfferSuccess -> showSuccessDialog = true
                    }
                }
            }
        }
    }
}
