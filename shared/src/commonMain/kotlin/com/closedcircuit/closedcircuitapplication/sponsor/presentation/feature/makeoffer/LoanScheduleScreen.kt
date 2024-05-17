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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.LoanBreakdown
import com.closedcircuit.closedcircuitapplication.common.presentation.components.LoanBreakdownType
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.components.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent


internal class LoanScheduleScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        ScreenContent(
            state = viewModel.loanScheduleState.value,
            goBack = navigator::pop
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