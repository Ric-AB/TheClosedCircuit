package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist

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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan.CreatePlanNavigator
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails.PlanDetailsScreen
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TaskLinearProgress
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

internal class PlanListScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PlanListViewModel>()
        val state = viewModel.stateFlow.collectAsState()
        ScreenContent(
            state = state.value,
            goBack = navigator::pop,
            navigateToCreatePlanScreen = { navigator.push(CreatePlanNavigator) },
            navigateToPlanDetailsScreen = { plan -> navigator.push(PlanDetailsScreen(plan)) }
        )
    }
}

@Composable
private fun ScreenContent(
    state: PlanListUiState,
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
                .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
        ) {
            items(state.plans) { plan ->
                PlanCard(
                    modifier = Modifier.fillMaxWidth(),
                    plan = plan,
                    onClick = { navigateToPlanDetailsScreen(plan) }
                )
            }
        }
    }
}

@Composable
private fun PlanCard(modifier: Modifier = Modifier, plan: Plan, onClick: () -> Unit) {
    val fundsRaised = remember {
        val value = (plan.totalFundsRaised / plan.targetAmount).value.toFloat()
        val percentage = value * 100
        Pair(value, "$percentage")
    }
    OutlinedCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            Avatar(
                imageUrl = plan.avatar.value,
                size = DpSize(50.dp, 50.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plan.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))
            BodyText(text = plan.description)

            Spacer(modifier = Modifier.height(16.dp))
            TaskLinearProgress(
                progress = fundsRaised.first,
                label = stringResource(
                    SharedRes.strings.x_percent_funds_raised_label,
                    fundsRaised.second
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            TaskLinearProgress(
                modifier = Modifier.fillMaxWidth(),
                progress = plan.tasksCompleted.toFloat(),
                label = stringResource(
                    SharedRes.strings.x_percent_tasks_completed_label,
                    plan.tasksCompletedAsPercentage
                )
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
