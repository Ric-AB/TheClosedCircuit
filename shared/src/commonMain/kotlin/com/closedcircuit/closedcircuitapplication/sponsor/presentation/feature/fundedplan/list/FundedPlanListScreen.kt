package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.FundedPlanItem
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class FundedPlanListScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FundedPlanListViewModel>()
        ScreenContent(state = viewModel.state.value, goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(state: FundedPlanListUiState, goBack: () -> Unit) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.funding_label),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {

                when (state) {
                    is FundedPlanListUiState.Content -> Body(state)
                    is FundedPlanListUiState.Error -> {}
                    FundedPlanListUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Body(state: FundedPlanListUiState.Content) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                vertical = verticalScreenPadding,
                horizontal = horizontalScreenPadding
            )
        ) {
            items(state.plans) { fundedPlanPreview ->
                FundedPlanItem(
                    modifier = Modifier.fillMaxWidth(),
                    beneficiaryFullName = fundedPlanPreview.beneficiaryFullName.value,
                    planImageUrl = fundedPlanPreview.avatar.value,
                    planSector = fundedPlanPreview.sector,
                    amountFunded = fundedPlanPreview.amountFunded.getFormattedValue(),
                    fundingType = fundedPlanPreview.fundingType.label,
                    fundsRaisedPercent = fundedPlanPreview.fundsRaisedPercent,
                    taskCompletedPercent = fundedPlanPreview.tasksCompletedPercent
                )
            }
        }
    }
}