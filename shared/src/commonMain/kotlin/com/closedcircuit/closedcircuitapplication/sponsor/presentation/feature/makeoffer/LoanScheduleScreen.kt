package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

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
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdown
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdownType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent


internal class LoanScheduleScreen : Screen, KoinComponent, CustomScreenTransition by SlideOverTransition {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        val state = viewModel.loanScheduleState.value
        val fundingItemsUiState = viewModel.fundingItemsState.value
        val fundingLevel = viewModel.fundingLevelState.fundingLevel
        val onEvent = viewModel::onEvent

        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
        ) {
            TitleText(stringResource(SharedRes.strings.loan_schedule_label))

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

            Spacer(Modifier.height(20.dp))
            DefaultButton(onClick = { onEvent(MakeOfferEvent.SubmitOffer) }) {
                Text(
                    stringResource(
                        SharedRes.strings.loan_x_label,
                        fundingItemsUiState.formattedTotal(fundingLevel)
                    )
                )
            }
        }
    }
}