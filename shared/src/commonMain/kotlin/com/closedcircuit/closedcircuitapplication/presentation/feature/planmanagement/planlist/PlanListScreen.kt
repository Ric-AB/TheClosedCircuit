package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.planlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.ProgressIndicator
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan.CreatePlanWrapperScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails.PlanDetailsScreen
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class PlanListScreen : Screen, KoinComponent {
    private val viewModel: PlanListViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.stateFlow.collectAsState()
        ScreenContent(
            state = state.value,
            goBack = navigator::pop,
            navigateToCreatePlanScreen = { navigator.push(CreatePlanWrapperScreen) },
            navigateToPlanDetailsScreen = { plan -> navigator.push(PlanDetailsScreen(plan)) }
        )
    }
}

@Composable
private fun ScreenContent(
    state: PlanListUIState,
    goBack: () -> Unit,
    navigateToCreatePlanScreen: () -> Unit,
    navigateToPlanDetailsScreen: (Plan) -> Unit
) {
    BaseScaffold(
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.plans),
                mainAction = goBack
            )
        },
        floatingActionButton = { PlansExtendedFab(onClick = navigateToCreatePlanScreen) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            items(state.plans) { plan ->
                PlanCard(
                    plan = plan,
                    modifier = Modifier.fillMaxWidth()
                        .clickable(enabled = true, onClick = { navigateToPlanDetailsScreen(plan) })
                )
            }
        }
    }
}

@Composable
private fun PlanCard(plan: Plan, modifier: Modifier) {
    val fundsRaised = remember {
        val value = (plan.fundsRaised / plan.targetAmount).value.toFloat()
        val percentage = value * 100
        Pair(value, "$percentage%")
    }
    OutlinedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            Avatar(
                avatar = plan.avatar,
                size = DpSize(50.dp, 50.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plan.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            BodyText(text = plan.description)

            Spacer(modifier = Modifier.height(16.dp))
            ProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = fundsRaised.first,
                displayText = stringResource(
                    SharedRes.strings.percentage_funds_raised,
                    fundsRaised.second
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            ProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = plan.tasksCompleted.toFloat(),
                displayText = stringResource(SharedRes.strings.percentage_task_completed, "50")
            )
        }
    }
}

@Composable
private fun PlansExtendedFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = { Text(text = stringResource(SharedRes.strings.new_plan)) },
        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
    )
}
