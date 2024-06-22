package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.WalletCard
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.FundedPlanItem
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

@OptIn(ExperimentalMaterial3Api::class)
object SponsorDashboardTab : Tab, KoinComponent {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(SharedRes.strings.home)
            val icon = rememberVectorPainter(Icons.Outlined.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = findRootNavigator(LocalNavigator.currentOrThrow)
        val messageBarState = rememberMessageBarState()
        val viewModel = getScreenModel<SponsorDashboardViewModel>()

        ScreenContent(
            messageBarState = messageBarState,
            state = viewModel.state,
        )
    }

    @Composable
    private fun ScreenContent(messageBarState: MessageBarState, state: SponsorDashboardUiState) {
        BaseScaffold(messageBarState = messageBarState) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = verticalScreenPadding)
            ) {
                WalletCard(
                    wallet = null,
                    modifier = Modifier.padding(horizontal = horizontalScreenPadding)
                )

                when (state) {
                    is SponsorDashboardUiState.Content -> {
                        Body(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = horizontalScreenPadding),
                            state = state,
                            navigateToPlanListScreen = {}
                        )
                    }

                    SponsorDashboardUiState.Empty -> {

                    }

                    is SponsorDashboardUiState.Error -> {
                        EmptyDashboard(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = horizontalScreenPadding),
                        )
                    }

                    SponsorDashboardUiState.Loading -> {
                        BackgroundLoader(Modifier.fillMaxWidth().weight(1f))
                    }
                }
            }
        }
    }

    @Composable
    private fun Body(
        modifier: Modifier,
        state: SponsorDashboardUiState.Content,
        navigateToPlanListScreen: () -> Unit,
    ) {
        Column(modifier = modifier) {
            val plans = state.plans
            val seeAllClick = if (plans.isNotEmpty()) navigateToPlanListScreen else null

            Spacer(Modifier.height(40.dp))
            SectionHeader(
                text = stringResource(SharedRes.strings.plans_funded_label),
                seeAllClick = seeAllClick
            )

            if (plans.isNotEmpty()) {
                plans.forEach { fundedPlanPreview ->
                    FundedPlanItem(
                        modifier = Modifier.fillMaxWidth(),
                        beneficiaryFullName = fundedPlanPreview.beneficiaryFullName.value,
                        planImageUrl = fundedPlanPreview.avatar.value,
                        planSector = fundedPlanPreview.sector,
                        amountFunded = fundedPlanPreview.amountFunded.getFormattedValue(),
                        fundingType = fundedPlanPreview.fundingType.displayText,
                        fundsRaisedPercent = fundedPlanPreview.fundsRaisedPercent,
                        taskCompletedPercent = fundedPlanPreview.tasksCompletedPercent
                    )
                }
            } else {
                NoPlan(modifier = Modifier.fillMaxWidth(), userFirstName = state.userFirstName)
            }
        }
    }

    @Composable
    private fun NoPlan(modifier: Modifier, userFirstName: String) {
        Card(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "No plans",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$userFirstName, you currently have not sponsored any plan yet.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

    @Composable
    private fun EmptyDashboard(modifier: Modifier) {
        Column(modifier = modifier) {

        }
    }

    @Composable
    private fun SectionHeader(
        text: String,
        seeAllClick: (() -> Unit)?
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (seeAllClick != null) {
                TextButton(
                    onClick = seeAllClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(SharedRes.strings.see_all_label),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun DashboardTopAppBar(navigateToNotificationScreen: () -> Unit) {
        TopAppBar(
            title = { },
            actions = {
                IconButton(onClick = navigateToNotificationScreen) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "notifications"
                    )
                }
            }
        )
    }
}