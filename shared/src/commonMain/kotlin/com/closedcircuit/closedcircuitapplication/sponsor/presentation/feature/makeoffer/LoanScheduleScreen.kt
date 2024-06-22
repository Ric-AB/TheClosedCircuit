package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdown
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdownType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.SuccessDialog
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class LoanScheduleScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        val messageBarState = rememberMessageBarState()
        var showSuccessDialog by remember { mutableStateOf(false) }

        viewModel.makeOfferResultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is MakeOfferResult.Error -> messageBarState.addError(it.message)
                MakeOfferResult.Success -> showSuccessDialog = true
            }
        }

        ScreenContent(
            state = viewModel.loanScheduleState.value,
            goBack = navigator::pop
        )

        SuccessDialog(
            visible = showSuccessDialog,
            isLoan = viewModel.fundType == FundType.LOAN,
            offeredAmount = viewModel.fundingItemsState.value.enteredAmount.value,
            beneficiaryName = "",
            onDismiss = { showSuccessDialog = false }
        )
    }

    @Composable
    private fun ScreenContent(state: LoanScheduleUiState, goBack: () -> Unit) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                TopAppBarTitle(stringResource(SharedRes.strings.loan_schedule_label))

                Spacer(Modifier.height(24.dp))
                LoanBreakdown(
                    modifier = Modifier.fillMaxWidth(),
                    type = LoanBreakdownType.LOAN,
                    data = persistentListOf(
                        state.loanAmount,
                        state.interestAmount,
                        state.repaymentAmount
                    )
                )

                Spacer(Modifier.height(16.dp))
                LoanBreakdown(
                    modifier = Modifier.fillMaxWidth(),
                    type = LoanBreakdownType.DURATION,
                    data = persistentListOf(
                        stringResource(SharedRes.strings.x_months, state.graceDuration),
                        stringResource(SharedRes.strings.x_months, state.repaymentDuration),
                        stringResource(SharedRes.strings.x_months, state.totalDuration),
                    )
                )

                Spacer(Modifier.height(16.dp))
                Table(
                    headerTableTitles = listOf("Dates", "Repayment amount"),
                    data = state.repaymentBreakdown
                )
            }
        }
    }
}