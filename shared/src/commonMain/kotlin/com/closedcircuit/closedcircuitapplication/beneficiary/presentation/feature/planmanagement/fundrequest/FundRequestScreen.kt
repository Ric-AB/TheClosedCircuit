package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LocalCurrencyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TextFieldDialogMenu
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PromptDialog
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TextFieldAffix
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.navigationResult
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.conditional
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.common.util.Empty
import com.closedcircuit.closedcircuitapplication.common.presentation.util.NumberCommaTransformation
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

internal class FundRequestScreen(private val plan: Plan, private val steps: Steps) : Screen,
    KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FundRequestViewModel> { parametersOf(plan) }
        val messageBarState = rememberMessageBarState()
        val paymentResult = navigator.navigationResult
            .getResult<Boolean>(PaymentScreen.PAYMENT_RESULT)

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is FundRequestResult.Error -> messageBarState.addError(it.message)
                is FundRequestResult.FundRequestSuccess ->
                    navigator.push(
                        FundRequestSummaryScreen(
                            fundRequestID = it.fundRequestID,
                            modeOfSupport = it.fundType,
                            plan = plan,
                            steps = steps
                        )
                    )

                is FundRequestResult.TokenizeRequestSuccess -> navigator.push(PaymentScreen(it.link))
            }
        }

        LaunchedEffect(paymentResult) {
            if (paymentResult.value == true) viewModel.onEvent(FundRequestUiEvent.SubmitFundRequest)
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
        state: FundRequestUiState,
        messageBarState: MessageBarState,
        goBack: () -> Unit,
        onEvent: (FundRequestUiEvent) -> Unit
    ) {
        BaseScaffold(
            topBar = { DefaultAppBar(mainAction = goBack) },
            showLoadingDialog = state.loading,
            messageBarState = messageBarState
        ) { innerPadding ->
            var showDialog by remember { mutableStateOf(false) }
            val commonModifier = Modifier.fillMaxWidth()

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
                    .imePadding()
            ) {
                TitleText(text = stringResource(SharedRes.strings.how_do_you_want_to_be_supported_label))

                Spacer(modifier = Modifier.height(8.dp))
                TextFieldDialogMenu(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(SharedRes.strings.select_category),
                    selectedItem = state.selectedFundType,
                    items = FundType.values().toList().toImmutableList(),
                    itemToString = { it.label },
                    onItemSelected = { _, item -> onEvent(FundRequestUiEvent.FundTypeChange(item)) },
                )

                AnimatedVisibility(state.showLoanSchedule) {
                    Column {
                        Spacer(modifier = Modifier.height(40.dp))
                        TitleText(stringResource(SharedRes.strings.loan_schedule_label))
                        BodyText(stringResource(SharedRes.strings.select_your_preferred_loan_schedule_label))

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = stringResource(SharedRes.strings.what_your_loan_range_label))
                        DefaultOutlinedTextField(
                            inputField = state.minimumLoanRange,
                            onValueChange = { onEvent(FundRequestUiEvent.MinRangeChange(it)) },
                            placeholder = { Text(stringResource(SharedRes.strings.minimum_amount_label)) },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            trailingIcon = { LocalCurrencyText() },
                            visualTransformation = NumberCommaTransformation(),
                            modifier = commonModifier.onFocusChanged {}
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        DefaultOutlinedTextField(
                            inputField = state.maximumLoanRange,
                            onValueChange = { onEvent(FundRequestUiEvent.MaxRangeChange(it)) },
                            placeholder = { Text(stringResource(SharedRes.strings.maximum_amount_label)) },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            trailingIcon = { LocalCurrencyText() },
                            visualTransformation = NumberCommaTransformation(),
                            modifier = commonModifier.onFocusChanged {}
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        TextFieldDialogMenu(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(SharedRes.strings.select_maximum_number_of_lenders_label),
                            selectedItem = state.numberOfLenders,
                            items = getNumberOfLenders(),
                            onItemSelected = { _, item ->
                                onEvent(FundRequestUiEvent.MaxLendersChange(item))
                            },
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        TextFieldDialogMenu(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(SharedRes.strings.select_grace_duration_label),
                            selectedItem = state.graceDuration,
                            items = getDurations(),
                            onItemSelected = { _, item ->
                                onEvent(FundRequestUiEvent.GraceDurationChange(item))
                            },
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        TextFieldDialogMenu(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(SharedRes.strings.select_repayment_duration_label),
                            selectedItem = state.repaymentDuration,
                            items = getDurations(),
                            onItemSelected = { _, item ->
                                onEvent(FundRequestUiEvent.RepaymentDurationChange(item))
                            },
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        TopLabeledTextField(
                            inputField = state.interestRate,
                            onValueChange = { onEvent(FundRequestUiEvent.InterestRateChange(it)) },
                            label = stringResource(SharedRes.strings.enter_loan_interest_rate_label),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            trailingIcon = { TextFieldAffix("%") },
                            modifier = commonModifier.onFocusChanged {}
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.conditional(
                        condition = state.showLoanSchedule,
                        ifTrue = { Modifier.height(40.dp) },
                        ifFalse = { Modifier.weight(1f) }
                    )
                )

                DefaultButton(
                    enabled = state.canSubmit,
                    onClick = {
                        if (state.canRequestFunds) onEvent(FundRequestUiEvent.SubmitFundRequest)
                        else showDialog = true
                    }
                ) {
                    Text(stringResource(SharedRes.strings.proceed))
                }

                PromptDialog(
                    visible = showDialog,
                    title = String.Empty,
                    message = stringResource(SharedRes.strings.tokenize_card_prompt),
                    buttonText = stringResource(SharedRes.strings.okay_label),
                    onDismiss = { showDialog = false },
                    onButtonClick = { onEvent(FundRequestUiEvent.TokenizeCard) }
                )
            }
        }
    }

    private fun getDurations(): ImmutableList<Int> {
        return persistentListOf(3, 6, 9, 12, 15, 18)
    }

    private fun getNumberOfLenders(): ImmutableList<Int> {
        return persistentListOf(1, 2, 3)
    }
}