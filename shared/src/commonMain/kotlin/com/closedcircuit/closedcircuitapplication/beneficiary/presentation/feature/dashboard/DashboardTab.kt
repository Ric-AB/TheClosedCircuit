@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan.CreatePlanNavigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist.PlanListScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.components.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.components.WalletCard
import com.closedcircuit.closedcircuitapplication.common.presentation.components.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification.NotificationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.accent1
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.accent2
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.accent3
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.accent4
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary2
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary3
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary4
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.secondary2
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.secondary4
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.secondary5
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.component.KoinComponent

internal object DashboardTab : Tab, KoinComponent {
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
        val viewModel = getScreenModel<DashboardViewModel>()

        ScreenContent(
            messageBarState = messageBarState,
            state = viewModel.uiState(),
            navigateToCreatePlan = { navigator.push(CreatePlanNavigator) },
            navigateToPlanListScreen = { navigator.push(PlanListScreen()) },
            navigateToNotificationScreen = { navigator.push(NotificationScreen()) },
            navigateToLoginScreen = { navigator.replaceAll(LoginScreen()) }
        )
    }

    @Composable
    private fun ScreenContent(
        messageBarState: MessageBarState,
        state: DashboardUiState,
        navigateToCreatePlan: () -> Unit,
        navigateToPlanListScreen: () -> Unit,
        navigateToNotificationScreen: () -> Unit,
        navigateToLoginScreen: () -> Unit
    ) {
        BaseScaffold(
            messageBarState = messageBarState,
            topBar = { DashboardTopAppBar(navigateToNotificationScreen = navigateToNotificationScreen) }
        ) { innerPadding ->
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
                    is DashboardUiState.Content -> {
                        LoadedDashboard(
                            modifier = Modifier.fillMaxWidth(),
                            state = state,
                            navigateToPlanListScreen = navigateToPlanListScreen,
                            navigateToLoginScreen = navigateToLoginScreen
                        )
                    }

                    DashboardUiState.Empty -> {
                        EmptyDashboard(
                            onClick = navigateToCreatePlan,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = horizontalScreenPadding),
                        )
                    }

                    DashboardUiState.Loading -> {
                        BackgroundLoader(Modifier.fillMaxWidth().weight(1f))
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadedDashboard(
        modifier: Modifier,
        state: DashboardUiState.Content,
        navigateToPlanListScreen: () -> Unit,
        navigateToLoginScreen: () -> Unit
    ) {
        Column(modifier = modifier) {
            val topSponsors = state.topSponsors
            if (topSponsors != null) {
                Spacer(Modifier.height(80.dp))
                TopSponsors(
                    topSponsors = topSponsors,
                    userFirstName = state.firstName,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            val completedPlans = state.completedPlansCount
            val onGoingPlans = state.ongoingPlansCount
            val notStartedPlans = state.notStartedPlansCount
            val showAnalytics = remember(completedPlans, onGoingPlans, notStartedPlans) {
                listOf(completedPlans, onGoingPlans, notStartedPlans).any { it > 0 }
            }

            if (showAnalytics) {
                Spacer(Modifier.height(32.dp))
                PlanAnalytics(
                    modifier = Modifier.fillMaxWidth(),
                    completedPlans = state.completedPlansCount,
                    onGoingPlans = state.ongoingPlansCount,
                    notStartedPlans = state.notStartedPlansCount
                )
            }

            val recentPlans = state.recentPlans
            if (recentPlans.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                RecentPlans(
                    recentPlans = recentPlans,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            val recentDonations = state.recentDonation
            if (state.recentDonation.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                RecentDonations(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = horizontalScreenPadding),
                    donations = recentDonations
                )
            }

            DefaultButton(onClick = navigateToPlanListScreen) {
                Text("Go")
            }

            DefaultButton(onClick = navigateToLoginScreen) {
                Text("Logout")
            }
        }
    }

    @Composable
    private fun RecentPlans(
        modifier: Modifier,
        recentPlans: ImmutableList<Plan>
    ) {
        Column(modifier = modifier) {
            SectionHeader(
                modifier = Modifier.padding(start = horizontalScreenPadding),
                text = stringResource(SharedRes.strings.plans),
                bottomPadding = 0.dp
            ) {
                TextButton(
                    onClick = {},
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(SharedRes.strings.see_all_label),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = horizontalScreenPadding)
            ) {
                items(items = recentPlans, key = { it.id.value }) {
                    ScreenCard(modifier = Modifier.width(250.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Avatar(avatar = it.avatar, size = DpSize(40.dp, 40.dp))

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = it.name)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(progress = it.fundsRaised.toFloat())
                            Text(
                                text = stringResource(
                                    SharedRes.strings.x_percent_funds_raised_label,
                                    it.fundsRaised.times(100)
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(progress = it.tasksCompleted.toFloat())
                            Text(
                                text = stringResource(
                                    SharedRes.strings.x_percent_tasks_completed_label,
                                    it.tasksCompleted.times(100)
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TopSponsors(
        modifier: Modifier,
        userFirstName: String,
        topSponsors: ImmutableList<Sponsor>
    ) {
        Column(modifier = modifier) {
            SectionHeader(
                modifier = Modifier.padding(start = horizontalScreenPadding),
                text = stringResource(SharedRes.strings.top_sponsors_label)
            )

            if (topSponsors.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = horizontalScreenPadding)
                ) {
                    items(topSponsors) {
                        Card(modifier = modifier.width(250.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                        .background(shape = Shapes().small, color = Color.White)
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = it.fullName.value)

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = it.loanAmount.value.toString(),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                NoSponsors(modifier = Modifier.fillMaxWidth(), userFirstName = userFirstName)
            }
        }
    }

    @Composable
    private fun NoSponsors(modifier: Modifier, userFirstName: String) {
        ScreenCard(modifier = modifier.padding(horizontal = horizontalScreenPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(SharedRes.strings.no_sponsors_label),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(SharedRes.strings.no_sponsors_message, userFirstName),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

    @Composable
    private fun RecentDonations(modifier: Modifier, donations: Donations) {
        Column(modifier = modifier) {
            SectionHeader(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.donations_label)
            )

            donations.forEach {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(avatar = it.sponsorAvatar, size = DpSize(40.dp, 40.dp))

                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = it.sponsorFullName.value,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = it.amount.value.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = it.planName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }

    }

    @Composable
    private fun EmptyDashboard(modifier: Modifier, onClick: () -> Unit) {
        Column(modifier = modifier) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val backgroundToTextColorPair = remember {
                    listOf(
                        primary3 to primary4,
                        secondary4 to secondary5,
                        accent2 to primary4,
                        secondary4 to secondary5,
                        accent3 to accent4,
                    )
                }

                val suggestions = remember {
                    listOf(
                        "A new personal business",
                        "A community project",
                        "A new social project with friends and family",
                        "Business Incubation",
                        "Business with friends and family",
                    )
                }

                suggestions.forEachIndexed { index, text ->
                    val usableIndex = index.coerceAtMost(backgroundToTextColorPair.lastIndex)
                    Text(
                        text = text,
                        color = backgroundToTextColorPair[usableIndex].second,
                        modifier = Modifier.clip(Shapes().small)
                            .background(backgroundToTextColorPair[usableIndex].first)
                            .clickable(onClick = onClick)
                            .padding(horizontal = 8.dp)
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


    @Composable
    private fun PlanAnalytics(
        modifier: Modifier,
        completedPlans: Int,
        onGoingPlans: Int,
        notStartedPlans: Int
    ) {
        Column(modifier = modifier.padding(horizontal = horizontalScreenPadding)) {
            SectionHeader(text = stringResource(SharedRes.strings.plan_analytic_label))
            ScreenCard(Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Row {
                        val values = persistentListOf(
                            onGoingPlans.toFloat(),
                            notStartedPlans.toFloat(),
                            completedPlans.toFloat()
                        )

                        val colors = persistentListOf(secondary2, accent1, primary2)
                        CircularProgressBar(
                            values = values,
                            colors = colors,
                            animationDuration = 1500,
                            modifier = Modifier
                        )

                        Spacer(Modifier.width(12.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(12.dp).align(Alignment.CenterVertically)
                        ) {
                            val commonModifier =
                                remember { Modifier.align(Alignment.CenterVertically) }
                            PlanCategory(
                                modifier = commonModifier,
                                color = primary2,
                                text = stringResource(SharedRes.strings.completed_label)
                            )

                            PlanCategory(
                                modifier = commonModifier,
                                color = secondary2,
                                text = stringResource(SharedRes.strings.ongoing_label)
                            )

                            PlanCategory(
                                modifier = commonModifier,
                                color = accent1,
                                text = stringResource(SharedRes.strings.not_started_label)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ScreenCard(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
        OutlinedCard(
            modifier = modifier,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary),
            content = content
        )
    }

    @Composable
    private fun SectionHeader(
        modifier: Modifier = Modifier,
        text: String,
        bottomPadding: Dp = 4.dp,
        action: @Composable () -> Unit = {}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth().padding(bottom = bottomPadding)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            action()
        }
    }

    @Composable
    private fun PlanCategory(modifier: Modifier = Modifier, color: Color, text: String) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )

            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    @Composable
    private fun CircularProgressBar(
        size: Dp = 80.dp,
        values: ImmutableList<Float>,
        colors: ImmutableList<Color>,
        animationDuration: Int,
        indicatorThickness: Dp = 12.dp,
        modifier: Modifier
    ) {
        val totalPlans = values.sum().toInt()
        var state by remember { mutableStateOf(values) }
        val animatedState = state.map {
            animateFloatAsState(targetValue = it, animationSpec = tween(animationDuration))
        }

        LaunchedEffect(key1 = values) {
            state = values.map { it * 100 / totalPlans }.toImmutableList()
        }

        val sweepAngles = animatedState.map { (360 * it.value / 100) }

        Box(
            modifier = modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size)) {
                var startAngle = -90f

                for (i in values.indices) {
                    drawArc(
                        color = colors[i],
                        startAngle = startAngle,
                        sweepAngle = sweepAngles[i],
                        useCenter = false,
                        style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngles[i]
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(SharedRes.strings.total_no_of_plans_label),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = totalPlans.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


