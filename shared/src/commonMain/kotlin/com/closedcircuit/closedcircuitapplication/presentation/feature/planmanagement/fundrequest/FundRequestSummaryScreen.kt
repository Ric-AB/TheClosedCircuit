package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.fundrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
    private val plan: Plan
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
                StepList(modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(24.dp))
                DefaultButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.share_plan_link_label))
                }

                Spacer(Modifier.height(4.dp))
                DefaultOutlinedButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.back_home_label))
                }
            }
        }
    }

    @Composable
    private fun PlanDetailsGrid(modifier: Modifier, plan: Plan) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = PaddingValues(12.dp)
        ) {
            item {
                GridItem(
                    imageResource = SharedRes.images.ic_briefcase,
                    header = plan.sector,
                    message = stringResource(SharedRes.strings.business_sector)
                )
            }

            item {
                GridItem(
                    imageResource = SharedRes.images.ic_calendar,
                    header = plan.duration.value.toString(),
                    message = stringResource(SharedRes.strings.plan_duration)
                )
            }

            item {
                GridItem(
                    imageResource = SharedRes.images.ic_target_amount,
                    header = plan.targetAmount.value.toString(),
                    message = stringResource(SharedRes.strings.target_amount)
                )
            }

            item {
                GridItem(
                    imageResource = SharedRes.images.ic_funds,
                    header = plan.totalFundsRaised.value.toString(),
                    message = stringResource(SharedRes.strings.total_funds_raised)
                )
            }

            item {
                GridItem(
                    imageResource = SharedRes.images.ic_orange_bag,
                    header = plan.estimatedCostPrice.value.toString(),
                    message = stringResource(SharedRes.strings.estimated_cost_price_per_unit_label)
                )
            }

            item {
                GridItem(
                    imageResource = SharedRes.images.ic_purple_file,
                    header = plan.estimatedSellingPrice.value.toString(),
                    message = stringResource(SharedRes.strings.estimated_selling_price_per_unit_label)
                )
            }
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
    private fun StepList(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader(stringResource(SharedRes.strings.steps))
            repeat(10) {
                StepItem()
                Divider()
            }
        }
    }

    @Composable
    private fun StepItem() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                BodyText("Conduct Market Research", color = Color.Black)

                Spacer(Modifier.height(4.dp))
                BodyText("$10,343")
            }
        }
    }
}
