package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PlanDetailsGrid
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.PlanImage
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval.StepApprovalScreen
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.component.PlanFundingTab
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.component.PlanProgressTab
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.component.PlanSummaryTab
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalFoundationApi::class)
internal class FundedPlanDetailsScreen(private val fundedPlanPreview: FundedPlanPreview) : Screen,
    KoinComponent, CustomScreenTransition by SlideOverTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel =
            getScreenModel<FundedPlanDetailsViewModel> { parametersOf(fundedPlanPreview) }

        ScreenContent(
            state = viewModel.state.value,
            goBack = navigator::pop,
            navigateToStepApproval = { fundedStep, canApprove ->
                navigator.push(
                    StepApprovalScreen(
                        planID = fundedStep.planID,
                        stepID = fundedStep.id,
                        isStepApprovedByUser = fundedStep.isApprovedByUser,
                        canApprove = canApprove
                    )
                )
            }
        )
    }

    @Composable
    private fun ScreenContent(
        state: FundedPlanDetailsUiState,
        goBack: () -> Unit,
        navigateToStepApproval: (FundedStepItem, Boolean) -> Unit
    ) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                when (state) {
                    is FundedPlanDetailsUiState.Content -> {
                        Body(
                            innerPadding = innerPadding,
                            state = state,
                            navigateToStepApproval = navigateToStepApproval
                        )
                    }

                    is FundedPlanDetailsUiState.Error -> {
                        Text(state.message)
                    }

                    FundedPlanDetailsUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Body(
        innerPadding: PaddingValues,
        state: FundedPlanDetailsUiState.Content,
        navigateToStepApproval: (FundedStepItem, Boolean) -> Unit
    ) {
        BoxWithConstraints(modifier = Modifier.padding(innerPadding)) {
            val screenHeight = maxHeight
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                Header(
                    planDescription = state.planDescription,
                    planImageUrl = state.planImageUrl,
                    planSector = state.planSector,
                    planDuration = state.planDuration.toString(),
                    estimatedCostPrice = state.estimatedCostPrice,
                    estimatedSellingPrice = state.estimatedSellingPrice,
                )

                Spacer(modifier = Modifier.height(16.dp))
                Tabs(
                    modifier = Modifier.height(screenHeight),
                    scrollState = scrollState,
                    state = state,
                    navigateToStepApproval = { navigateToStepApproval(it, state.canApprove) }
                )
            }
        }
    }

    @Composable
    private fun Header(
        planDescription: String,
        planImageUrl: String,
        planSector: String,
        planDuration: String,
        estimatedCostPrice: String,
        estimatedSellingPrice: String
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PlanDetailsGrid(
                modifier = Modifier.fillMaxWidth(),
                sector = planSector,
                duration = planDuration,
                estimatedCostPrice = estimatedCostPrice,
                estimatedSellingPrice = estimatedSellingPrice,
                targetAmount = null,
                totalFundsRaised = null
            )

            Spacer(Modifier.height(20.dp))
            PlanImage(
                imageUrl = planImageUrl,
                modifier = Modifier.fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(SharedRes.strings.plan_description_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(4.dp))
            BodyText(planDescription)
        }
    }

    @Composable
    private fun Tabs(
        modifier: Modifier,
        scrollState: ScrollState,
        state: FundedPlanDetailsUiState.Content,
        navigateToStepApproval: (FundedStepItem) -> Unit,
    ) {
        val list = listOf(
            stringResource(SharedRes.strings.plan_summary_label),
            stringResource(SharedRes.strings.plan_funding_label),
            stringResource(SharedRes.strings.plan_progress_label),
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
                pageNestedScrollConnection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            return if (available.y > 0) Offset.Zero else Offset(
                                x = 0f,
                                y = -scrollState.dispatchRawDelta(-available.y)
                            )
                        }
                    }
                }
            ) { page: Int ->

                val commonModifier = remember {
                    Modifier.fillMaxWidth().padding(top = verticalScreenPadding)
                }

                when (page) {
                    0 -> {
                        PlanSummaryTab(
                            modifier = commonModifier,
                            items = state.stepsWithBudgets,
                            total = state.itemsTotal
                        )
                    }

                    1 -> {
                        PlanFundingTab(
                            modifier = commonModifier,
                            planSector = state.planSector,
                            amountFunded = state.amountFunded,
                            fundingType = state.fundingType,
                            fundingLevel = state.fundingLevel,
                            fundingDate = state.fundingDate,
                            itemsTotal = state.itemsTotal,
                            stepsWithBudgets = state.stepsWithBudgets,
                            navigateToFundPlan = {}
                        )
                    }

                    2 -> {
                        PlanProgressTab(
                            modifier = commonModifier,
                            stepItems = state.fundedStepItems,
                            stepItemsWithProofs = state.fundedStepItemsWithProofs,
                            navigateToStepApproval = navigateToStepApproval
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}