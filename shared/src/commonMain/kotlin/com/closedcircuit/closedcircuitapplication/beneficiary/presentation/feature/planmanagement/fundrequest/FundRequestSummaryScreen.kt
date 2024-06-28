package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.PlanDetailsGrid
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class FundRequestSummaryScreen(
    private val modeOfSupport: FundType,
    private val plan: Plan,
    private val steps: Steps
) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(goBack: () -> Unit) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.plan_summary_label),
                    mainAction = goBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                PlanDetailsGrid(
                    modifier = Modifier.fillMaxWidth(),
                    sector = plan.sector,
                    duration = plan.duration.value.toString(),
                    estimatedCostPrice = plan.estimatedCostPrice.value.toString(),
                    estimatedSellingPrice = plan.estimatedSellingPrice.value.toString(),
                    targetAmount = plan.targetAmount.value.toString(),
                    totalFundsRaised = plan.totalFundsRaised.value.toString()
                )

                Spacer(Modifier.height(24.dp))
                HeaderWithMessage(
                    modifier = Modifier.fillMaxWidth(),
                    header = stringResource(SharedRes.strings.plan_description_label),
                    message = plan.description
                )

                Spacer(Modifier.height(24.dp))
                HeaderWithMessage(
                    modifier = Modifier.fillMaxWidth(),
                    header = stringResource(SharedRes.strings.mode_of_support_label),
                    message = modeOfSupport.label
                )

                Spacer(Modifier.height(24.dp))
                StepList(modifier = Modifier.fillMaxWidth(), steps = steps)

                Spacer(Modifier.height(24.dp))
                DefaultButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.share_plan_link_label))
                }

                Spacer(Modifier.height(8.dp))
                DefaultOutlinedButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.back_home_label))
                }
            }
        }
    }

    @Composable
    private fun HeaderWithMessage(modifier: Modifier, header: String, message: String) {
        Column(modifier = modifier) {
            SectionHeader(header)
            Spacer(Modifier.height(4.dp))
            BodyText(text = message)
        }
    }

    @Composable
    private fun SectionHeader(header: String) {
        Text(
            text = header,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    private fun StepList(modifier: Modifier = Modifier, steps: Steps) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader(stringResource(SharedRes.strings.steps))
            steps.forEach {
                StepItem(it)
                HorizontalDivider()
            }
        }
    }

    @Composable
    private fun StepItem(step: Step) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                BodyText(step.name, color = Color.Black)

                Spacer(Modifier.height(4.dp))
                BodyText(step.targetFunds.getFormattedValue())
            }
        }
    }
}
