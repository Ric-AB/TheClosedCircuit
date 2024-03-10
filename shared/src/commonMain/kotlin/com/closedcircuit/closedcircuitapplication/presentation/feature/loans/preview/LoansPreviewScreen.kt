package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.loan.LoanPreview
import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.CircularIndicator
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.component.KoinComponent


internal class LoansPreviewScreen(private val loanStatus: LoanStatus) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoansPreviewViewModel>()

        LaunchedEffect(Unit) {
            viewModel.onEvent(LoansPreviewUiEvent.Fetch(loanStatus))
        }

        ScreenContent(state = viewModel.uiState(), goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(state: LoansPreviewUiState, goBack: () -> Unit) {
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
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                when (state) {
                    is LoansPreviewUiState.DataLoaded -> LoanPreviews(
                        modifier = Modifier.fillMaxWidth(),
                        items = state.items
                    )

                    is LoansPreviewUiState.Error -> {}
                    LoansPreviewUiState.Loading -> Loader(Modifier.fillMaxSize())
                }
            }
        }
    }

    @Composable
    private fun LoanPreviews(modifier: Modifier, items: ImmutableList<LoanPreview>) {
        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items) { loanPreview ->
                Preview(modifier = Modifier.fillMaxWidth(), loanPreview = loanPreview)
            }
        }
    }

    @Composable
    private fun Preview(modifier: Modifier, loanPreview: LoanPreview) {

        @Composable
        fun RowItem(label: String, value: String, valueFontWeight: FontWeight = FontWeight.Medium) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    color = Color.Gray
                )

                Text(text = value, fontWeight = valueFontWeight)
            }
        }

        Column(modifier = modifier) {
            Text(text = loanPreview.planName)

            Spacer(Modifier.height(8.dp))
            Card {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    RowItem(
                        label = stringResource(SharedRes.strings.total_sponsors),
                        value = stringResource(
                            SharedRes.strings.x_sponsor,
                            loanPreview.totalSponsors
                        )
                    )

                    RowItem(
                        label = stringResource(SharedRes.strings.total_amount),
                        value = loanPreview.totalAmountOffered.value.toString(),
                        valueFontWeight = FontWeight.SemiBold
                    )

                    Box {
                        val avatarSize = 24
                        loanPreview.sponsorAvatars.forEachIndexed { index, avatar ->
                            Avatar(
                                avatar = avatar,
                                size = DpSize(avatarSize.dp, avatarSize.dp),
                                modifier = Modifier.offset(x = ((index * avatarSize) / 2).dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Loader(modifier: Modifier) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularIndicator(size = 50.dp)
        }
    }
}