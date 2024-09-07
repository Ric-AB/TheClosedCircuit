package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentScreen
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class MakeOfferNavigator(private val planID: ID) : Screen,
    KoinComponent {
    @Composable
    override fun Content() {
        var showSuccessDialog by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(false) }
        val messageBarState = rememberMessageBarState()

        Navigator(PlanSummaryScreen(planID)) { navigator ->
            BaseScaffold(
                topBar = {
                    DefaultAppBar(
                        mainAction = navigator::pop,
                        mainIcon = navigator.takeIf { it.canPop }
                            ?.let { Icons.AutoMirrored.Rounded.ArrowBack }
                    )
                },
                showLoadingDialog = loading,
                messageBarState = messageBarState
            ) { innerPadding ->
                ScreenBasedTransition(
                    navigator = navigator,
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )

                val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
                val loadingState = viewModel.loading.value
                remember(loadingState) { loading = loadingState }
                viewModel.makeOfferResultChannel.receiveAsFlow().observeWithScreen { result ->
                    when (result) {
                        is MakeOfferResult.Error -> messageBarState.addError(result.message)
                        is MakeOfferResult.DonationOfferSuccess ->
                            navigator.push(PaymentScreen(result.paymentLink))

                        MakeOfferResult.LoanOfferSuccess -> showSuccessDialog = true
                    }
                }
            }
        }
    }
}
