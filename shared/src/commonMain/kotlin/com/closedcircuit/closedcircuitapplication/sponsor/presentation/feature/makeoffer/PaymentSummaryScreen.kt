package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.components.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
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
            onEvent = viewModel::onEvent,
            navigateToLoanTerms = { navigator.push(LoanTermsScreen()) })
    }

    @Composable
    private fun ScreenContent(
        selectedFundingLevel: FundingLevel,
        state: FundingItemsUiState,
        goBack: () -> Unit,
        onEvent: (MakeOfferEvent) -> Unit,
        navigateToLoanTerms: () -> Unit
    ) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                var showDonatePromptDialog by remember { mutableStateOf(false) }
                var showDonateDialog by remember { mutableStateOf(false) }
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
                DefaultButton(onClick = {
                    onEvent(MakeOfferEvent.FundTypeChange(FundType.DONATION))
                    // todo show donation dialog
                }) {
                    Text(
                        stringResource(
                            SharedRes.strings.donate_x_label,
                            state.formattedTotalOfSelectedItems
                        )
                    )
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
                            navigateToLoanTerms()
                        }
                    ) {
                        Text(
                            stringResource(
                                SharedRes.strings.loan_x_label,
                                state.formattedTotalOfSelectedItems
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
                            onEvent(MakeOfferEvent.FundTypeChange(FundType.LOAN))
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
                    onDismiss = { showDonatePromptDialog = false },
                    onClick = onClick
                )

                DonateDialog(
                    visible = showDonateDialog,
                    amount = state.formattedTotalOfSelectedItems,
                    onDismiss = { showDonateDialog = false },
                    onClick = { onEvent(MakeOfferEvent.SubmitOffer) }
                )
            }
        }
    }

    @Composable
    private fun LoanErrorDialog(
        visible: Boolean,
        prompt: String,
        onDismiss: () -> Unit,
        onClick: (() -> Unit)?
    ) {
        if (visible) {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = Shapes().medium,
                    modifier = Modifier.width(350.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 19.dp).padding(bottom = 20.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }

                        Image(
                            painter = painterResource(SharedRes.images.ic_red_caution),
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        if (onClick != null) {
                            Spacer(modifier = Modifier.height(40.dp))
                            DefaultButton(onClick = { onDismiss(); onClick() }) {
                                Text(stringResource(SharedRes.strings.donate_x_label))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DonateDialog(
        visible: Boolean,
        amount: String,
        onDismiss: () -> Unit,
        onClick: () -> Unit
    ) {
        if (visible) {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = Shapes().medium,
                    modifier = Modifier.width(350.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 19.dp)
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
                        Text(
                            text = stringResource(
                                SharedRes.strings.you_are_about_to_donate_prompt,
                                amount
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                        DefaultButton(onClick = onClick) {
                            Text(stringResource(SharedRes.strings.proceed))
                        }
                    }
                }
            }
        }
    }
}