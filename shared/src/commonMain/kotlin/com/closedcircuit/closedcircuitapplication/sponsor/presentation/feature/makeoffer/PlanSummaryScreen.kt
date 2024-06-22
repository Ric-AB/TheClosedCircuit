package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PlanDetailsGrid
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary2
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary3
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepsWithBudgetTable
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class PlanSummaryScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        ScreenContent(
            state = viewModel.planSummaryState,
            goBack = navigator::pop,
            navigateToSelectFundingLevel = { navigator.push(FundingLevelScreen()) }
        )
    }

    @Composable
    private fun ScreenContent(
        state: PlanSummaryUiState,
        goBack: () -> Unit,
        navigateToSelectFundingLevel: () -> Unit
    ) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                when (state) {
                    is PlanSummaryUiState.Content -> Content(state, navigateToSelectFundingLevel)
                    is PlanSummaryUiState.Error -> Text(state.message)
                    PlanSummaryUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Content(
        state: PlanSummaryUiState.Content,
        navigateToSelectFundingLevel: () -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
        ) {
            TitleText(stringResource(SharedRes.strings.how_can_you_help_label))
            BodyText(stringResource(SharedRes.strings.how_can_you_help_message_label))

            Spacer(Modifier.height(24.dp))
            TitleText(stringResource(SharedRes.strings.plan_summary_label))

            Spacer(Modifier.height(16.dp))
            PlanDetailsGrid(
                modifier = Modifier.fillMaxWidth(),
                sector = state.businessSector,
                duration = state.planDuration,
                estimatedCostPrice = state.estimatedCostPrice,
                estimatedSellingPrice = state.estimatedSellingPrice,
                targetAmount = null,
                totalFundsRaised = null
            )

            Spacer(Modifier.height(16.dp))
            CoilImage(
                imageModel = { state.planImage },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            TopAppBarTitle(text = stringResource(SharedRes.strings.plan_description_label))
            BodyText(text = state.planDescription)

            Spacer(modifier = Modifier.height(16.dp))
            ProfitProgressBar(
                progress = state.estimatedProfitFraction,
                percentageValue = state.estimatedProfitPercent
            )

            Spacer(modifier = Modifier.height(16.dp))
            TitleText(stringResource(SharedRes.strings.plan_steps_and_budgets_label))

            Spacer(modifier = Modifier.height(16.dp))
            StepsWithBudgetTable(items = state.stepsWithBudgets, total = state.total)

            Spacer(Modifier.height(40.dp))
            DefaultButton(onClick = navigateToSelectFundingLevel) {
                Text(stringResource(SharedRes.strings.proceed))
            }
        }
    }

    @Composable
    private fun ProfitProgressBar(
        progress: Float,
        percentageValue: String,
        size: Dp = 170.dp,
        indicatorThickness: Dp = 20.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(size)) {
                    val canvasSize = this.size.minDimension

                    val radius =
                        canvasSize / 2 - maxOf(indicatorThickness, indicatorThickness).toPx() / 2
                    drawCircle(
                        color = primary2,
                        radius = radius,
                        center = this.size.center,
                        style = Stroke(
                            width = indicatorThickness.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(8f, 10f),
                                phase = 0f
                            )
                        )
                    )
                }
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .size(size),
                    color = primary3,
                    strokeWidth = indicatorThickness
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(color = Color.Transparent, CircleShape)
                        .size(size - (indicatorThickness * 4))
                ) {
                    Text(
                        text = "$percentageValue %",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Text(
                text = stringResource(SharedRes.strings.estimated_profit_label),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}