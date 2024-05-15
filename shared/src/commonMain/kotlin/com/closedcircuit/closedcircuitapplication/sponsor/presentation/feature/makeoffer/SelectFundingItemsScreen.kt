package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent


internal class SelectFundingItemsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MakeOfferViewModel>()
//        ScreenContent(viewModel.planSummaryState, goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(state: SelectFundingLevelUiState, goBack: () -> Unit) {
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
                SelectItemTable(persistentListOf())
            }
        }
    }

    @Composable
    private fun SelectItemTable(items: ImmutableList<FundingItem>) {
        OutlinedCard(
            shape = Shapes().medium,
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray,
            ),
        ) {
            Column {
                val commonModifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                val contentAlignment = Alignment.Center
                SelectItemTableHeader(
                    modifier = commonModifier,
                    contentAlignment = contentAlignment
                )

                SelectItemTableBody(
                    modifier = commonModifier,
                    contentAlignment = contentAlignment,
                    items = items
                )
            }
        }
    }

    @Composable
    private fun SelectItemTableBody(
        modifier: Modifier,
        contentAlignment: Alignment,
        items: ImmutableList<FundingItem>
    ) {
        Column {
            items.forEachIndexed { _, fundingItem ->
                Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                    val rowData = listOf(fundingItem.name, fundingItem.cost, "")
                    rowData.forEachIndexed { index, text ->
                        val weight = if (index == 1) 8f else 2f
                        Box(
                            modifier = Modifier.weight(weight),
                            contentAlignment = contentAlignment,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .height(38.dp)
                                        .wrapContentHeight()
                                        .padding(end = 8.dp),
                                    textAlign = TextAlign.Start,
                                )

                                if (index == rowData.lastIndex) {
                                    Checkbox(checked = fundingItem.isSelected, onCheckedChange = {})
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SelectItemTableHeader(modifier: Modifier, contentAlignment: Alignment) {
        Column {
            Row(modifier) {
                val titles = listOf("Steps", "Cost", "Select all")
                titles.forEachIndexed { index, title ->
                    val weight = if (index == 1) 8f else 2f
                    Box(
                        modifier = Modifier
                            .weight(weight),
                        contentAlignment = contentAlignment,
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .height(38.dp)
                                .wrapContentHeight(),
                            textAlign = TextAlign.Start,
                        )
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