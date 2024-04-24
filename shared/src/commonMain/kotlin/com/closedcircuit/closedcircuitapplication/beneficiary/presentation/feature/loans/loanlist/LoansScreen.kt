package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.loanlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.Loan
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.components.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.details.LoanDetailsScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.component.KoinComponent


internal class LoansScreen(private val planID: ID, private val loanStatus: LoanStatus) : Screen,
    KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoansViewModel>()

        LaunchedEffect(Unit) {
            viewModel.onEvent(LoansUiEvent.Fetch(planID, loanStatus))
        }

        ScreenContent(
            state = viewModel.uiState(),
            goBack = navigator::pop,
            navigateToLoanDetails = { navigator.push(LoanDetailsScreen(it)) }
        )
    }

    @Composable
    private fun ScreenContent(
        state: LoansUiState,
        goBack: () -> Unit,
        navigateToLoanDetails: (ID) -> Unit
    ) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.loans_label),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (state) {
                    is LoansUiState.Content -> LoanItems(
                        modifier = Modifier.fillMaxWidth(),
                        items = state.items,
                        navigateToLoanDetails = navigateToLoanDetails
                    )

                    is LoansUiState.Error -> {}
                    LoansUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun LoanItems(
        modifier: Modifier = Modifier,
        items: ImmutableList<Loan>,
        navigateToLoanDetails: (ID) -> Unit
    ) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                horizontal = horizontalScreenPadding,
                vertical = verticalScreenPadding
            )
        ) {
            items(items) { loan ->
                LoanItem(
                    modifier = Modifier.fillMaxWidth(),
                    loan = loan,
                    onClick = { navigateToLoanDetails(loan.loanId) }
                )
            }
        }
    }

    @Composable
    private fun LoanItem(modifier: Modifier, loan: Loan, onClick: () -> Unit) {
        @Composable
        fun RowItem(label: String, value: String) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = label)
                Spacer(Modifier.width(4.dp))
                Text(text = value)
            }
        }

        OutlinedCard(modifier = modifier) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(avatar = loan.sponsorAvatar, size = DpSize(50.dp, 50.dp))

                    Spacer(Modifier.width(8.dp))
                    Text(text = loan.sponsorFullName.value)
                }

                RowItem(
                    label = stringResource(SharedRes.strings.amount_label),
                    value = loan.loanAmount.value.toString()
                )

                RowItem(
                    label = stringResource(SharedRes.strings.grace_period_label),
                    value = loan.gracePeriod.toString()
                )

                RowItem(
                    label = stringResource(SharedRes.strings.loan_percentage_label),
                    value = loan.interestRate.toString()
                )

                RowItem(
                    label = stringResource(SharedRes.strings.date_label),
                    value = loan.createdAt.format(Date.Format.dd_mmm_yyyy)
                )

                Spacer(Modifier.height(8.dp))
                DefaultOutlinedButton(onClick = onClick) {
                    Text(stringResource(SharedRes.strings.view_offer_label))
                }
            }
        }
    }
}