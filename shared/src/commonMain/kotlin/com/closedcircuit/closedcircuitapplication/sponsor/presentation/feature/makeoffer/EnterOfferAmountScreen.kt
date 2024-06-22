package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.presentation.util.NumberCommaTransformation
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.DonateDialog
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.LoanErrorDialog
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.SuccessDialog
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class EnterOfferAmountScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val messageBarState = rememberMessageBarState()
        val viewModel = getScreenModel<MakeOfferViewModel>()
        var showSuccessDialog by remember { mutableStateOf(false) }

        viewModel.makeOfferResultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is MakeOfferResult.Error -> messageBarState.addError(it.message)
                MakeOfferResult.Success -> showSuccessDialog = true
            }
        }

        ScreenContent(
            state = viewModel.fundingItemsState.value,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent
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
    private fun ScreenContent(
        state: FundingItemsUiState,
        goBack: () -> Unit,
        onEvent: (MakeOfferEvent) -> Unit
    ) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                var showDonationDialog by remember { mutableStateOf(false) }
                var showMaxRangeErrorDialog by remember { mutableStateOf(false) }

                DefaultOutlinedTextField(
                    inputField = InputField(),
                    onValueChange = {},
                    label = stringResource(SharedRes.strings.enter_amount_label),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = NumberCommaTransformation()
                )

                DefaultButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.donate_label))
                }

                if (state.canOfferLoan) {
                    DefaultOutlinedButton(onClick = {}) {
                        Text(stringResource(SharedRes.strings.loan_label))
                    }
                }

                if (state.canOfferLoan && state.isEnteredAmountBelowMinAmount) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(SharedRes.strings.loan_range_below_min_prompt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                DonateDialog(
                    visible = showDonationDialog,
                    amount = state.enteredAmount.value,
                    onDismiss = { showDonationDialog = false },
                    onPrimaryClick = {
                        onEvent(MakeOfferEvent.FundTypeChange(FundType.DONATION))
                        onEvent(MakeOfferEvent.SubmitOffer)
                    }
                )

                LoanErrorDialog(
                    visible = showMaxRangeErrorDialog,
                    prompt = stringResource(SharedRes.strings.loan_range_above_max_prompt),
                    onDismiss = { showMaxRangeErrorDialog = false },
                    onClick = null
                )
            }
        }
    }
}