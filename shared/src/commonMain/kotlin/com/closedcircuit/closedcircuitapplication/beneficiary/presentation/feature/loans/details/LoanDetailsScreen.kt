package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.details

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanSchedule
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdown
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdownType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class LoanDetailsScreen(private val loanID: ID) : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoanDetailsViewModel> { parametersOf(loanID) }
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(Unit) {
            viewModel.onEvent(LoanDetailsUiEvent.Fetch)
        }

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is LoanDetailsResult.Error -> messageBarState.addError(it.message)
                LoanDetailsResult.Success -> navigator.pop()
            }
        }

        ScreenContent(
            state = viewModel.uiState(),
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
            RepaymentSchedule(state.repaymentSchedule)

            if (state.canTransact) {
                Spacer(Modifier.height(40.dp))
                DefaultButton(onClick = { onEvent(LoanDetailsUiEvent.Submit(LoanStatus.ACCEPTED)) }) {
                    Text(stringResource(SharedRes.strings.accept_offer_label))
                }

                Spacer(Modifier.height(8.dp))
                DefaultOutlinedButton(onClick = { onEvent(LoanDetailsUiEvent.Submit(LoanStatus.DECLINED)) }) {
                    Text(stringResource(SharedRes.strings.decline_offer_label))
                }
            }
        }
    }

    @Composable
    private fun RepaymentSchedule(repaymentSchedule: ImmutableList<LoanSchedule>) {
        val headerTitles = persistentListOf(
            stringResource(SharedRes.strings.date_label),
            stringResource(SharedRes.strings.repayment_amount_label)
        )

        Table(
            data = repaymentSchedule,
            headerTableTitles = headerTitles,
        )
    }
}