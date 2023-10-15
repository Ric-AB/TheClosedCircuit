@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.user.Sponsor
import com.closedcircuit.closedcircuitapplication.domain.wallet.Wallet
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.notification.NotificationScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.planlist.PlanListScreen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.findRootNavigator
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object DashboardTab : Tab, KoinComponent {
    private val viewModel: DashboardViewModel by inject()
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
        val uiState by viewModel.state.collectAsState()
        val messageBarState = rememberMessageBarState()

        ScreenContent(
            messageBarState = messageBarState,
            uiState = uiState,
            navigateToPlanListScreen = { navigator.push(PlanListScreen()) },
            navigateToNotificationScreen = { navigator.push(NotificationScreen()) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: DashboardUIState,
    navigateToPlanListScreen: () -> Unit,
    navigateToNotificationScreen: () -> Unit
) {
    BaseScaffold(
        messageBarState = messageBarState,
        topBar = { DashboardTopAppBar(navigateToNotificationScreen = navigateToNotificationScreen) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                WalletCard(
                    wallet = uiState.wallet,
                    modifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)
                )
            }

            item {
                val topSponsors = uiState.topSponsors
                if (topSponsors != null) {
                    TopSponsors(
                        topSponsors = topSponsors,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }

            item {
                val recentPlans = uiState.recentPlans
                if (recentPlans.isNotEmpty()) {
                    RecentPlans(
                        recentPlans = recentPlans,
                        modifier = Modifier.animateItemPlacement(),
                        headerModifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)
                    )
                }
            }

            item {
                DefaultButton(onClick = navigateToPlanListScreen) {
                    Text("Go")
                }
            }
        }
    }
}

private val screenCardElevation = Elevation.Level2

@Composable
private fun NoSponsors(modifier: Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = screenCardElevation),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "No sponsors",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Patience, you currently do not have any Sponsors. Share your link to family and friends.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun RecentPlans(
    recentPlans: ImmutableList<Plan>,
    modifier: Modifier,
    headerModifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = headerModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Plans",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            TextButton(
                onClick = {},
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = "See all",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Rounded.ArrowForward, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = defaultHorizontalScreenPadding)
        ) {
            items(recentPlans) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = screenCardElevation),
                    modifier = Modifier.width(250.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = it.name)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(progress = 0.5f)
                    Text(
                        text = "50% funds raised",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(progress = 0.5f)
                    Text(
                        text = "50% tasks completed",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopSponsors(topSponsors: ImmutableList<Sponsor>, modifier: Modifier) {
    if (topSponsors.isNotEmpty()) {
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
                    Text(text = "Jane Cooper")

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "NGN 50,000.00", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    } else {
        NoSponsors(modifier = modifier)
    }
}

@Composable
private fun WalletCard(wallet: Wallet?, modifier: Modifier) {
    @Composable
    fun Card(modifier: Modifier, containerColor: Color) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {}
    }

    Box(
        modifier = modifier.fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        val commonModifier = Modifier.padding(horizontal = 8.dp)
            .matchParentSize()

        Card(
            modifier = commonModifier.rotate(5F),
            containerColor = Color.LightGray
        )

        Card(
            modifier = commonModifier.rotate(355F),
            containerColor = Color.LightGray
        )

        Card(
            modifier = Modifier.matchParentSize(),
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )

        Text(
            text = wallet?.totalFunds?.value?.toString() ?: "--",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
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