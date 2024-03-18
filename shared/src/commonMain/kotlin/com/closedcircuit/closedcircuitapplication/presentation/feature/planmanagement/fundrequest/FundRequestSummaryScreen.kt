package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.fundrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import com.closedcircuit.closedcircuitapplication.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import com.closedcircuit.closedcircuitapplication.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedButton
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
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
                PlanDetailsGrid(modifier = Modifier.fillMaxWidth(), plan = plan)

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
                    message = modeOfSupport.displayText
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
    private fun PlanDetailsGrid(modifier: Modifier, plan: Plan) {

        @Composable
        fun GridRow(item1: @Composable () -> Unit, item2: @Composable () -> Unit) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    item1()
                }

                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    item2()
                }
            }
        }


        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GridRow(
                item1 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_briefcase,
                        header = plan.sector,
                        message = stringResource(SharedRes.strings.business_sector)
                    )
                },
                item2 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_calendar,
                        header = plan.duration.value.toString(),
                        message = stringResource(SharedRes.strings.plan_duration)
                    )
                }
            )


            GridRow(
                item1 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_target_arrow,
                        header = plan.targetAmount.value.toString(),
                        message = stringResource(SharedRes.strings.target_amount)
                    )
                },
                item2 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_funds,
                        header = plan.totalFundsRaised.value.toString(),
                        message = stringResource(SharedRes.strings.total_funds_raised)
                    )
                }
            )

            GridRow(
                item1 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_orange_bag,
                        header = plan.estimatedCostPrice.value.toString(),
                        message = stringResource(SharedRes.strings.estimated_cost_price_per_unit_label)
                    )
                },
                item2 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_purple_file,
                        header = plan.estimatedSellingPrice.value.toString(),
                        message = stringResource(SharedRes.strings.estimated_selling_price_per_unit_label)
                    )
                }
            )
        }
    }

    @Composable
    private fun GridItem(imageResource: ImageResource, header: String, message: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(imageResource),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = header,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
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
                Divider()
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
                BodyText(step.targetFunds.value.toString())
            }
        }
    }
}
