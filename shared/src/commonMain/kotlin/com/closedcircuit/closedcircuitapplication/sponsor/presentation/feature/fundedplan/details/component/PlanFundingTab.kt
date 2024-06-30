package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepItem
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepsWithBudgetTable
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanFundingTab(
    modifier: Modifier = Modifier,
    planSector: String,
    amountFunded: String,
    fundingType: String,
    fundingLevel: String,
    fundingDate: String,
    itemsTotal: String,
    stepsWithBudgets: ImmutableMap<StepItem, ImmutableList<BudgetItem>>,
    navigateToFundPlan: () -> Unit
) {
    val list = listOf(
        stringResource(SharedRes.strings.funded_plan_label),
        stringResource(SharedRes.strings.general_funding_label),
    )

    val pagerState = rememberPagerState(initialPage = 0) { list.size }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {},
            modifier = Modifier.fillMaxWidth(),
            indicator = { _ -> }
        ) {
            list.forEachIndexed { index, text ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = text, color = Color(0xff6FAAEE)) }
                )
            }
        }

        HorizontalPager(
            pageSpacing = horizontalScreenPadding,
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { page: Int ->

            val commonModifier = remember {
                Modifier.fillMaxWidth().padding(top = verticalScreenPadding)
            }

            when (page) {
                0 -> {
                    FundedPlanTab(
                        modifier = commonModifier,
                        planSector = planSector,
                        amountFunded = amountFunded,
                        fundingType = fundingType,
                        fundingLevel = fundingLevel,
                        fundingDate = fundingDate,
                        navigateToFundPlan = {}
                    )
                }

                1 -> {
                    GeneralFundingTab(
                        modifier = commonModifier,
                        stepsWithBudgets = stepsWithBudgets,
                        total = itemsTotal,
                        navigateToFundPlan = navigateToFundPlan
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun GeneralFundingTab(
    modifier: Modifier,
    stepsWithBudgets: ImmutableMap<StepItem, ImmutableList<BudgetItem>>,
    total: String,
    navigateToFundPlan: () -> Unit
) {
    LazyColumn(modifier) {
        item {
            StepsWithBudgetTable(items = stepsWithBudgets, total = total, showFundingStatus = true)
        }

        item {
            Spacer(Modifier.height(40.dp))
            DefaultButton(onClick = navigateToFundPlan) {
                Text(stringResource(SharedRes.strings.fund_plan_label))
            }
        }
    }
}

@Composable
private fun FundedPlanTab(
    modifier: Modifier,
    planSector: String,
    amountFunded: String,
    fundingType: String,
    fundingLevel: String,
    fundingDate: String,
    navigateToFundPlan: () -> Unit
) {
    @Composable
    fun ItemText(text: AnnotatedString) {
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }

    Column(modifier = modifier) {
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ItemText(
                    text = getAnnotatedString(
                        label = stringResource(SharedRes.strings.plan_sector_colon_label),
                        value = planSector
                    )
                )

                ItemText(
                    text = getAnnotatedString(
                        label = stringResource(SharedRes.strings.amount_funded_colon_label),
                        value = amountFunded
                    )
                )

                ItemText(
                    text = getAnnotatedString(
                        label = stringResource(SharedRes.strings.funding_type_colon_label),
                        value = fundingType
                    )
                )

                ItemText(
                    text = getAnnotatedString(
                        label = stringResource(SharedRes.strings.funding_level_colon_label),
                        value = fundingLevel
                    )
                )

                ItemText(
                    text = getAnnotatedString(
                        label = stringResource(SharedRes.strings.funding_date_colon_label),
                        value = fundingDate
                    )
                )
            }
        }

        Spacer(Modifier.height(40.dp))
        DefaultButton(onClick = navigateToFundPlan) {
            Text(stringResource(SharedRes.strings.fund_plan_label))
        }
    }
}

private fun getAnnotatedString(label: String, value: String): AnnotatedString {
    return buildAnnotatedString {
        append(label)
        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append(value)
        }
    }
}