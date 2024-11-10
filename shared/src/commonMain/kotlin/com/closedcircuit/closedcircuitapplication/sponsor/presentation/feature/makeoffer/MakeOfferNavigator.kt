package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.AppExtendedFabWithLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation.ConversationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.navigationResult
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.component.SuccessDialog
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class MakeOfferNavigator(private val planID: ID) : Screen,
    KoinComponent {
    @Composable
    override fun Content() {
        val messageBarState = rememberMessageBarState()
        var showSuccessDialog by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(false) }
        var loadingUser by remember { mutableStateOf(false) }
        var onEvent: ((MakeOfferEvent) -> Unit)? = null

        Navigator(PlanSummaryScreen(planID)) { navigator ->
            val rootNavigator = remember(navigator) { findRootNavigator(navigator) }
            BaseScaffold(
                topBar = {
                    DefaultAppBar(
                        mainAction = navigator::pop,
                        mainIcon = navigator.takeIf { it.canPop }
                            ?.let { Icons.AutoMirrored.Rounded.ArrowBack }
                    )
                },
                showLoadingDialog = loading,
                messageBarState = messageBarState,
                floatingActionButton = {
                    StartChatFab(
                        show = navigator.lastItem is PlanSummaryScreen || navigator.lastItem is LoanTermsScreen,
                        showLoader = loadingUser,
                        onClick = { onEvent?.invoke(MakeOfferEvent.FetchChatUser) }
                    )
                }
            ) { innerPadding ->
                ScreenBasedTransition(
                    navigator = navigator,
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )

                val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
                onEvent = viewModel::onEvent
                val paymentResult = navigator.navigationResult
                    .getResult<Boolean>(PaymentScreen.PAYMENT_RESULT)
                val loadingState = remember(viewModel.loading.value, viewModel.loadingUser.value) {
                    Pair(viewModel.loading.value, viewModel.loadingUser.value)
                }

                LaunchedEffect(loadingState) {
                    loading = loadingState.first
                    loadingUser = loadingState.second
                }

                LaunchedEffect(paymentResult) {
                    if (paymentResult.value == true) {
                        showSuccessDialog = true
                    }
                }

                viewModel.makeOfferResultChannel.receiveAsFlow().observeWithScreen { result ->
                    when (result) {
                        is MakeOfferResult.Error -> messageBarState.addError(result.message)
                        is MakeOfferResult.DonationOfferSuccess ->
                            navigator.push(PaymentScreen(result.paymentLink))

                        is MakeOfferResult.ChatUserSuccess ->
                            rootNavigator.push(ConversationScreen(result.chatUser))

                        MakeOfferResult.LoanOfferSuccess -> showSuccessDialog = true
                    }
                }

                if (showSuccessDialog) {
                    SuccessDialog(
                        visible = showSuccessDialog,
                        isLoan = viewModel.fundType == FundType.LOAN,
                        offeredAmount = viewModel.fundingItemsState.value.enteredAmount.value,
                        beneficiaryName = (viewModel.planSummaryState as? PlanSummaryUiState.Content)?.planOwnerFullName.orEmpty(),
                        onDismiss = { showSuccessDialog = false }
                    )
                }
            }
        }
    }

    @Composable
    private fun StartChatFab(show: Boolean, showLoader: Boolean, onClick: () -> Unit) {
        if (show) {
            AppExtendedFabWithLoader(
                onClick = onClick,
                text = { Text(text = stringResource(SharedRes.strings.start_chat_label)) },
                icon = {
                    if (showLoader) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            painter = painterResource(SharedRes.images.ic_square_pen),
                            contentDescription = null
                        )
                    }
                },
                showLoader = showLoader
            )
        }
    }
}
