package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Shapes
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.PlanDetailsGrid
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.skydoves.landscapist.coil3.CoilImage
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


@OptIn(ExperimentalFoundationApi::class)
internal class FundedPlanDetailsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FundedPlanDetailsViewModel>()
        ScreenContent(state = viewModel.state.value, goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(state: FundedPlanDetailsUiState, goBack: () -> Unit) {
        BaseScaffold { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                when (state) {
                    is FundedPlanDetailsUiState.Content -> Body(innerPadding, state)
                    is FundedPlanDetailsUiState.Error -> {}
                    FundedPlanDetailsUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Body(innerPadding: PaddingValues, state: FundedPlanDetailsUiState.Content) {
        BoxWithConstraints(modifier = Modifier.padding(innerPadding)) {
            val screenHeight = maxHeight
            val screenWidth = maxWidth
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                Header(
                    planDescription = state.planDescription,
                    planImageUrl = state.planImageUrl,
                    planSector = state.planSector,
                    planDuration = state.planDuration.value.toString(),
                    targetAmount = state.targetAmount,
                    totalFundsRaised = state.totalFundsRaised,
                )

                Spacer(modifier = Modifier.height(16.dp))
                FundedPlanTabs(
                    modifier = Modifier.height(screenHeight),
                    scrollState = scrollState,
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
        targetAmount: String,
        totalFundsRaised: String
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PlanDetailsGrid(
                modifier = Modifier.fillMaxWidth(),
                sector = planSector,
                duration = planDuration,
                targetAmount = targetAmount,
                totalFundsRaised = totalFundsRaised,
                estimatedCostPrice = null,
                estimatedSellingPrice = null
            )

            Spacer(Modifier.height(20.dp))
            CoilImage(
                imageModel = { planImageUrl },
                modifier = Modifier.fillMaxWidth()
                    .height(150.dp)
                    .clip(Shapes().small)
            )

            Spacer(Modifier.height(20.dp))
            TitleText(stringResource(SharedRes.strings.plan_description_label))
            BodyText(planDescription)
        }
    }

    @Composable
    private fun FundedPlanTabs(modifier: Modifier, scrollState: ScrollState) {
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
                state = pagerState,
                modifier = Modifier
                    .fillMaxHeight()
                    .nestedScroll(
                        remember {
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
                        })
            ) { page: Int ->
                val listModifier = Modifier.fillMaxSize()
                    .padding(horizontal = 12.dp)

                when {
                    page == 0 -> {
//                        StepList(
//                            modifier = listModifier,
//                            steps = steps,
//                            navigateToStepDetails = navigateToStepDetails
//                        )
                    }

                    page == 1 -> {
//                        BudgetList(modifier = listModifier, budgets = budgets)
                    }
                }
            }
        }
    }
}