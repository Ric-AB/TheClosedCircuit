package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.EmptyScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdown
import com.closedcircuit.closedcircuitapplication.common.presentation.component.LoanBreakdownType
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.delayPop
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.navigationResult
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
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
        val paymentResult = navigator.navigationResult
            .getResult<Boolean>(PaymentScreen.PAYMENT_RESULT)

        var showSuccessDialog by remember { mutableStateOf(false) }

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                LoanDetailsResult.CancelSuccess -> {
                    navigator.delayPop()
                }

                is LoanDetailsResult.Error -> messageBarState.addError(it.message)
                is LoanDetailsResult.InitiatePaymentSuccess -> {
                    navigator.push(PaymentScreen(it.paymentLink))
                }
            }
        }

        LaunchedEffect(paymentResult) {
            if (paymentResult.value == true) {
                showSuccessDialog = true
            }
        }

        ScreenContent(
            state = viewModel.state,
            messageBarState = messageBarState,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent
        )

        PaymentSuccessDialog(
            visible = showSuccessDialog,
            state = viewModel.state as? LoanDetailsUiState.Content,
            onDismiss = { showSuccessDialog = false; navigator.pop() }
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

                    is LoanDetailsUiState.Error -> {
                        EmptyScreen(
                            title = stringResource(SharedRes.strings.oops_label),
                            message = state.message
                        )
                    }

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
                    state.formattedLoanAmount,
                    state.interestAmount,
                    state.repaymentAmount
                )
            )

            Spacer(Modifier.height(20.dp))
            LoanBreakdown(
                modifier = Modifier.fillMaxWidth(),
                type = LoanBreakdownType.DURATION,
                data = persistentListOf(
                    state.formattedLoanAmount,
                    state.interestAmount,
                    state.repaymentAmount
                )
            )

            if (state.canInitiatePayment) {
                Spacer(Modifier.height(40.dp))
                DefaultOutlinedButton(onClick = { onEvent(LoanDetailsUiEvent.InitiatePayment) }) {
                    Text(stringResource(SharedRes.strings.proceed_to_pay_label))
                }
            }

            if (state.canCancelOffer) {
                Spacer(Modifier.height(20.dp))
                DefaultOutlinedButton(onClick = { onEvent(LoanDetailsUiEvent.Cancel) }) {
                    Text(stringResource(SharedRes.strings.cancel_label))
                }
            }
        }
    }

    @Composable
    private fun PaymentSuccessDialog(
        visible: Boolean,
        state: LoanDetailsUiState.Content?,
        modifier: Modifier = Modifier,
        onDismiss: () -> Unit
    ) {
        if (visible && state != null) {
            Dialog(
                onDismissRequest = { onDismiss() },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier.size(320.dp, 340.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 12.dp)
                    ) {
                        IconButton(
                            onClick = { onDismiss() },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Image(
                            painter = painterResource(SharedRes.images.ic_thank_you_icon),
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(
                                SharedRes.strings.payment_made_message,
                                state.formattedLoanAmount,
                                state.repaymentAmount,
                                state.repaymentDuration,
                                state.graceDuration
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}