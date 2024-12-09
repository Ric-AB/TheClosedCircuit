package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loanlist

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.EmptyScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.getEmptyStateText
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanOffer
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails.LoanDetailsScreen
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class LoansScreen(private val loanStatus: LoanStatus) : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoansViewModel> { parametersOf(loanStatus) }
        ScreenContent(
            state = viewModel.state,
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

                    is LoansUiState.Error -> {
                        EmptyScreen(
                            title = stringResource(SharedRes.strings.oops_label),
                            message = state.message
                        )
                    }

                    LoansUiState.Empty -> {
                        val (titleRes, messageRes) = getEmptyStateText(loanStatus)
                        EmptyScreen(
                            imageSize = 200.dp,
                            image = painterResource(SharedRes.images.empty_loans_illurstration),
                            title = stringResource(titleRes),
                            message = stringResource(messageRes)
                        )
                    }

                    LoansUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun LoanItems(
        modifier: Modifier = Modifier,
        items: ImmutableList<LoanOffer>,
        navigateToLoanDetails: (ID) -> Unit
    ) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(
                horizontal = horizontalScreenPadding,
                vertical = verticalScreenPadding
            )
        ) {
            items(items) { loanOffer ->
                LoanItem(
                    modifier = Modifier.fillMaxWidth(),
                    loanOffer = loanOffer,
                    onClick = { navigateToLoanDetails(loanOffer.loanOfferId!!) }
                )
            }
        }
    }

    @Composable
    private fun LoanItem(modifier: Modifier, loanOffer: LoanOffer, onClick: () -> Unit) {
        @Composable
        fun RowItem(label: String, value: String) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = label, color = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(text = value, color = Color.Black)
            }
        }

        Row(modifier = modifier) {
            Avatar(imageUrl = loanOffer.beneficiaryAvatar!!.value, size = DpSize(50.dp, 50.dp))
            Spacer(Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RowItem(
                    label = "Business name:",
                    value = loanOffer.businessName.toString()
                )

                RowItem(
                    label = "Beneficiary:",
                    value = loanOffer.beneficiaryName!!.value
                )

                RowItem(
                    label = "Plan sector:",
                    value = loanOffer.planSector.toString()
                )

                RowItem(
                    label = "Loan amount:",
                    value = loanOffer.loanAmount?.getFormattedValue().toString()
                )

                RowItem(
                    label = stringResource(SharedRes.strings.date_label),
                    value = loanOffer.createdAt?.format(Date.Format.dd_mmm_yyyy).toString()
                )

                Spacer(Modifier.height(8.dp))
                DefaultOutlinedButton(onClick = onClick) {
                    Text(stringResource(SharedRes.strings.view_offer_label))
                }
            }
        }
    }
}