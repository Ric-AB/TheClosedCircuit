package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class FundingItemsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        ScreenContent(
            state = viewModel.fundingItemsState.value,
            selectedFundingLevel = viewModel.fundingLevelState.fundingLevel!!,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent,
            navigateToPaymentSummary = {
                viewModel.onEvent(MakeOfferEvent.CreateSchedule)
                navigator.push(PaymentSummaryScreen())
            }
        )
    }

    @Composable
    private fun ScreenContent(
        state: FundingItemsUiState,
        selectedFundingLevel: FundingLevel,
        goBack: () -> Unit,
        onEvent: (MakeOfferEvent) -> Unit,
        navigateToPaymentSummary: () -> Unit
    ) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                TopAppBarTitle(stringResource(SharedRes.strings.how_would_you_like_to_sponsor_label))
                BodyText(stringResource(SharedRes.strings.select_budgets_you_wish_to_fund_label))

                Spacer(Modifier.height(24.dp))
                SelectItemTable(
                    fundingLevelLabel = selectedFundingLevel.getLabel(),
                    isAllSelected = state.allItemsSelected,
                    items = state.availableItems,
                    onEvent = onEvent
                )

                Spacer(Modifier.height(40.dp))
                DefaultButton(onClick = navigateToPaymentSummary, enabled = state.canProceed) {
                    Text(stringResource(SharedRes.strings.proceed))
                }
            }
        }
    }

    @Composable
    private fun SelectItemTable(
        fundingLevelLabel: String,
        isAllSelected: Boolean,
        items: SnapshotStateList<FundingItem>,
        onEvent: (MakeOfferEvent) -> Unit
    ) {
        OutlinedCard(
            shape = Shapes().medium,
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray,
            ),
        ) {
            Column {
                val commonModifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                val contentAlignment = Alignment.CenterStart
                SelectItemTableHeader(
                    fundingLevelLabel = fundingLevelLabel,
                    modifier = commonModifier,
                    contentAlignment = contentAlignment,
                    isAllSelected = isAllSelected,
                    onToggle = { onEvent(MakeOfferEvent.ToggleAllFundingItems) }
                )

                SelectItemTableBody(
                    modifier = commonModifier,
                    contentAlignment = contentAlignment,
                    items = items,
                    onToggle = { onEvent(MakeOfferEvent.ToggleFundingItem(it)) }
                )
            }
        }
    }

    @Composable
    private fun SelectItemTableBody(
        items: SnapshotStateList<FundingItem>,
        modifier: Modifier,
        contentAlignment: Alignment,
        onToggle: (Int) -> Unit,
    ) {
        Column {
            items.forEachIndexed { itemIndex, fundingItem ->
                Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                    val rowData = listOf(fundingItem.name, fundingItem.formattedCost, "")
                    rowData.forEachIndexed { rowDataIndex, text ->
                        val weight = if (rowDataIndex == rowData.lastIndex) 2f else 3f
                        Box(
                            modifier = Modifier.weight(weight),
                            contentAlignment = contentAlignment,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodySmall,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .height(38.dp)
                                        .wrapContentHeight(),
                                    textAlign = TextAlign.Start,
                                )

                                if (rowDataIndex == rowData.lastIndex) {
                                    val isSelectable = remember {
                                        derivedStateOf {
                                            if (itemIndex == 0) true
                                            else {
                                                val previousIndex = itemIndex - 1
                                                items[previousIndex].isSelected
                                            }
                                        }
                                    }

                                    Checkbox(
                                        checked = fundingItem.isSelected,
                                        onCheckedChange = { onToggle(itemIndex) },
                                        enabled = isSelectable.value,
                                        modifier = Modifier.clip(Shapes().medium)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SelectItemTableHeader(
        fundingLevelLabel: String,
        isAllSelected: Boolean,
        modifier: Modifier,
        contentAlignment: Alignment,
        onToggle: () -> Unit
    ) {
        Column {
            Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                val titles = listOf(
                    fundingLevelLabel,
                    stringResource(SharedRes.strings.cost_label),
                    stringResource(SharedRes.strings.select_all_label)
                )
                titles.forEachIndexed { index, title ->
                    val weight = if (index == titles.lastIndex) 2f else 3f
                    Box(
                        modifier = Modifier
                            .weight(weight),
                        contentAlignment = contentAlignment,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .height(38.dp)
                                    .wrapContentHeight(),
                                textAlign = TextAlign.Start,
                            )

                            if (index == titles.lastIndex) {
                                Checkbox(
                                    checked = isAllSelected,
                                    onCheckedChange = { onToggle() },
                                    modifier = Modifier.clip(Shapes().medium)
                                )
                            }
                        }
                    }
                }
            }

            Divider(
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}