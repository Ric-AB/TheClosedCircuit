package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.domain.model.FundingLevel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.DonateDialog
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.LoanErrorDialog
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class PaymentSummaryScreen : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        val selectedFundingLevel = viewModel.fundingLevelState.fundingLevel!!
        val state = viewModel.fundingItemsState.value
        val onEvent = viewModel::onEvent

        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
        ) {
            var showDonatePromptDialog by remember { mutableStateOf(false) }
            var showDonateDialog by remember { mutableStateOf(false) }
            TitleText(stringResource(SharedRes.strings.payment_summary_label))

            Spacer(Modifier.height(24.dp))
            Table(
                data = state.selectedItems,
                headerTableTitles = listOf(
                    selectedFundingLevel.label,
                    stringResource(SharedRes.strings.cost_label)
                ),
                footerTableTitles = listOf(
                    stringResource(SharedRes.strings.total_label),
                    state.formattedTotal(selectedFundingLevel)
                )
            )

            Spacer(Modifier.height(24.dp))
            if (state.canOfferDonation) {
                Spacer(Modifier.height(16.dp))
                DefaultButton(
                    onClick = {
                        onEvent(MakeOfferEvent.FundTypeChange(FundType.DONATION))
                        showDonateDialog = true
                    }
                ) {
                    Text(
                        stringResource(
                            SharedRes.strings.donate_x_label,
                            state.formattedTotal(selectedFundingLevel)
                        )
                    )
                }
            }

            if (state.canOfferLoan) {
                Spacer(Modifier.height(16.dp))
                DefaultOutlinedButton(
                    onClick = {
                        if (state.isBelowMinimumAmount) {
                            showDonatePromptDialog = true
                            return@DefaultOutlinedButton
                        }

                        if (state.isAboveMaximumAmount) {
                            showDonatePromptDialog = true
                            return@DefaultOutlinedButton
                        }
                        onEvent(MakeOfferEvent.FundTypeChange(FundType.LOAN))
                        navigator.push(LoanTermsScreen())
                    }
                ) {
                    Text(
                        stringResource(
                            SharedRes.strings.loan_x_label,
                            state.formattedTotal(selectedFundingLevel)
                        )
                    )
                }
            }

            val (prompt, onClick) = remember {
                if (state.isAboveMaximumAmount) {
                    val prompt = SharedRes.strings.loan_range_above_max_prompt
                    val onClick = null
                    Pair(prompt, onClick)
                } else {
                    val prompt = SharedRes.strings.loan_range_below_min_prompt
                    val onClick = {
                        onEvent(MakeOfferEvent.FundTypeChange(FundType.DONATION))
                        showDonateDialog = true
                    }

                    Pair(prompt, onClick)
                }
            }

            LoanErrorDialog(
                visible = showDonatePromptDialog,
                prompt = stringResource(
                    prompt,
                    state.minLoanAmount,
                    state.maxLoanAmount
                ),
                donationAmount = state.formattedTotal(selectedFundingLevel),
                onDismiss = { showDonatePromptDialog = false },
                onClick = onClick
            )

            DonateDialog(
                visible = showDonateDialog,
                amount = state.formattedTotal(selectedFundingLevel),
                onDismiss = { showDonateDialog = false },
                onPrimaryClick = { onEvent(MakeOfferEvent.SubmitOffer) }
            )
        }
    }

    @Composable
    private fun ScreenContent(
        selectedFundingLevel: FundingLevel,
        state: FundingItemsUiState,
        onEvent: (MakeOfferEvent) -> Unit,
        navigateToLoanTerms: () -> Unit
    ) {

    }
}