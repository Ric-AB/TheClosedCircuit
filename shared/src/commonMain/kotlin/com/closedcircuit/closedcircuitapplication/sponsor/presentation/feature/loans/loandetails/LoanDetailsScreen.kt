package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.LoanBreakdown
import com.closedcircuit.closedcircuitapplication.common.presentation.components.LoanBreakdownType
import com.closedcircuit.closedcircuitapplication.common.presentation.components.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.components.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class LoanDetailsScreen(private val loanID: ID) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoanDetailsViewModel> { parametersOf(loanID) }
        val messageBarState = rememberMessageBarState()

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                LoanDetailsResult.CancelSuccess -> {
                    delay(300)
                    navigator.pop()
                }

                is LoanDetailsResult.Error -> messageBarState.addError(it.message)
                is LoanDetailsResult.InitiatePaymentSuccess -> {}
            }
        }

        ScreenContent(
            state = viewModel.state,
            messageBarState = messageBarState,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent
        )
    }

    @Composable
    private fun ScreenContent(
        state: LoanDetailsUiState,
        messageBarState: MessageBarState,
        goBack: () -> Unit,
        onEvent: (LoanDetailsUiEvent) -> Unit
    ) {
        BaseScaffold(
            showLoadingDialog = state.postLoading,
            messageBarState = messageBarState,
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.loan_details_label),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                when (state) {
                    is LoanDetailsUiState.Content ->
                        Body(
                            state = state,
                            onEvent = onEvent,
                            modifier = Modifier.fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(
                                    horizontal = horizontalScreenPadding,
                                    vertical = verticalScreenPadding
                                )
                        )

                    is LoanDetailsUiState.Error -> {}
                    LoanDetailsUiState.Loading -> BackgroundLoader()
                }
            }

        }
    }

    @Composable
    private fun Body(
        modifier: Modifier,
        state: LoanDetailsUiState.Content,
        onEvent: (LoanDetailsUiEvent) -> Unit
    ) {
        Column(modifier = modifier) {
            LoanBreakdown(
                modifier = Modifier.fillMaxWidth(),
                type = LoanBreakdownType.LOAN,
                data = persistentListOf(
                    state.loanAmount,
                    state.interestAmount,
                    state.repaymentAmount
                )
            )

            Spacer(Modifier.height(20.dp))
            LoanBreakdown(
                modifier = Modifier.fillMaxWidth(),
                type = LoanBreakdownType.DURATION,
                data = persistentListOf(
                    state.loanAmount,
                    state.interestAmount,
                    state.repaymentAmount
                )
            )

            if (state.canInitiatePayment) {
                Spacer(Modifier.height(40.dp))
                DefaultOutlinedButton(onClick = { onEvent(LoanDetailsUiEvent.InitiatePayment) }) {
                    Text(stringResource(SharedRes.strings.proceed_to_pay_label))
                }
            }

            if (state.canCancelOffer) {
                Spacer(Modifier.height(20.dp))
                DefaultOutlinedButton(onClick = { onEvent(LoanDetailsUiEvent.Cancel) }) {
                    Text(stringResource(SharedRes.strings.cancel_label))
                }
            }
        }
    }
}