package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.components.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class PaymentSummaryScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        ScreenContent(
            selectedFundingLevel = viewModel.fundingLevelState.fundingLevel!!,
            state = viewModel.fundingItemsState,
            goBack = navigator::pop,
            navigateToLoanTerms = { navigator.push(LoanTermsScreen()) })
    }

    @Composable
    private fun ScreenContent(
        selectedFundingLevel: FundingLevel,
        state: FundingItemsUiState,
        goBack: () -> Unit,
        navigateToLoanTerms: () -> Unit
    ) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                TopAppBarTitle(stringResource(SharedRes.strings.payment_summary_label))

                Spacer(Modifier.height(24.dp))
                Table(
                    data = state.selectedItems,
                    headerTableTitles = listOf(
                        selectedFundingLevel.getLabel(),
                        stringResource(SharedRes.strings.cost_label)
                    ),
                    footerTableTitles = listOf(
                        stringResource(SharedRes.strings.total_label),
                        state.formattedTotalOfSelectedItems
                    ),
                    contentAlignment = Alignment.CenterStart
                )

                Spacer(Modifier.height(40.dp))
                DefaultButton(onClick = {}) {
                    Text(
                        stringResource(
                            SharedRes.strings.donate_x_label,
                            state.formattedTotalOfSelectedItems
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))
                DefaultOutlinedButton(onClick = navigateToLoanTerms) {
                    Text(
                        stringResource(
                            SharedRes.strings.loan_x_label,
                            state.formattedTotalOfSelectedItems
                        )
                    )
                }
            }
        }
    }
}