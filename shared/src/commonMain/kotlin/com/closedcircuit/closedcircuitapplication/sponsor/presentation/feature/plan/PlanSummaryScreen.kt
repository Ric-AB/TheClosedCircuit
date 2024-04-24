package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.plan

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.components.PlanDetailsGrid
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary2
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary3
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class PlanSummaryScreen(private val plan: Plan) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(goBack: () -> Unit) {
        BaseScaffold { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                TitleText(stringResource(SharedRes.strings.how_can_you_help_label))
                Text(stringResource(SharedRes.strings.how_can_you_help_message_label))

                Spacer(Modifier.height(16.dp))
                TitleText(stringResource(SharedRes.strings.plan_summary_label))
                PlanDetailsGrid(modifier = Modifier.fillMaxWidth(), plan = plan, miniMode = true)

                // todo replace with image
                Spacer(Modifier.fillMaxWidth().height(100.dp))

                Text(
                    text = stringResource(SharedRes.strings.plan_description_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // todo move to viewmodel
                val estimatedCost = plan.estimatedCostPrice.value
                val estimatedSellingPrice = plan.estimatedSellingPrice.value
                val progress = remember {
                    ((estimatedSellingPrice - estimatedCost) / estimatedCost).toFloat()
                }

                Spacer(modifier = Modifier.height(16.dp))
                ProfitProgressBar(progress = progress)

            }
        }
    }

    @Composable
    fun ProfitProgressBar(progress: Float, size: Dp = 170.dp, indicatorThickness: Dp = 20.dp) {
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
                                floatArrayOf(8f, 10f), 0f
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
                        text = "${(progress * 100).toDouble()} %",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Text(
                text = stringResource(SharedRes.strings.estimated_profit_label),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

    }
}