package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanSchedule
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.components.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.components.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class LoanDetailsScreen(private val loanID: ID) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoanDetailsViewModel> { parametersOf(loanID) }
        val messageBarState = rememberMessageBarState()
        ScreenContent(viewModel.state, messageBarState, goBack = navigator::pop, viewModel::onEvent)
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
                    LoanDetailsUiState.Loading -> BackgroundLoader(Modifier.fillMaxWidth())
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
            LoanSummary(
                modifier = Modifier.fillMaxWidth(),
                loanAmount = state.loanAmount,
                interestAmount = state.interestAmount,
                repaymentAmount = state.repaymentAmount
            )

            Spacer(Modifier.height(40.dp))
            RepaymentSchedule(state.repaymentSchedule)

            if (state.canTransact) {
                Spacer(Modifier.height(40.dp))
                DefaultOutlinedButton(onClick = { onEvent(LoanDetailsUiEvent.Cancel) }) {
                    Text(stringResource(SharedRes.strings.accept_offer_label))
                }
            }
        }
    }

    @Composable
    private fun LoanSummary(
        modifier: Modifier,
        loanAmount: String,
        interestAmount: String,
        repaymentAmount: String
    ) {
        @Composable
        fun RowScope.ColumnItem(label: String, value: String, weight: Float) {
            Column(modifier = Modifier.weight(weight)) {
                BodyText(label)
                Text(
                    text = value,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        val outlineColor = MaterialTheme.colorScheme.primary
        OutlinedCard(
            modifier = modifier,
            border = BorderStroke(
                width = CardDefaults.outlinedCardBorder().width,
                color = outlineColor
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                ColumnItem(
                    label = stringResource(SharedRes.strings.loan_label),
                    value = loanAmount,
                    weight = 1f
                )

                Text(
                    text = "+",
                    color = outlineColor
                )

                ColumnItem(
                    label = stringResource(SharedRes.strings.interest_label),
                    value = interestAmount,
                    1f
                )

                Text(
                    text = "=",
                    color = outlineColor
                )

                ColumnItem(
                    label = stringResource(SharedRes.strings.total_repayment_label),
                    value = repaymentAmount,
                    weight = 2f
                )
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
            disableVerticalDividers = true
        )
    }
}