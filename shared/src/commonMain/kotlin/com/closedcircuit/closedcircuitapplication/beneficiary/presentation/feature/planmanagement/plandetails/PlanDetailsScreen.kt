@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.editplan.EditPlanScreen
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest.FundRequestScreen
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep.SaveStepScreen
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails.StepDetailsScreen
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

internal data class PlanDetailsScreen(val plan: Plan) : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PlanDetailsViewModel> { parametersOf(plan) }
        val uiState by viewModel.state.collectAsState()
        ScreenContent(
            uiState = uiState,
            goBack = navigator::pop,
            navigateToStepDetails = { navigator.push(StepDetailsScreen(it)) },
            navigateToFundRequest = { navigator.push(FundRequestScreen(plan, uiState.steps)) },
            navigateToEditPlan = { navigator.push(EditPlanScreen(uiState.plan)) },
            navigateToSaveStep = { navigator.push(SaveStepScreen(plan.id)) }
        )
    }

    @Composable
    private fun ScreenContent(
        uiState: PlanDetailsUiState,
        goBack: () -> Unit,
        navigateToStepDetails: (Step) -> Unit,
        navigateToFundRequest: () -> Unit,
        navigateToEditPlan: () -> Unit,
        navigateToSaveStep: () -> Unit
    ) {
        BaseScaffold(
            topBar = {
                PlanDetailsAppBar(
                    onNavClick = goBack,
                    navigateToFundRequest = navigateToFundRequest,
                    navigateToEditPlan = navigateToEditPlan
                )
            },
            floatingActionButton = { PlanDetailsExtendedFab(navigateToSaveStep) }
        ) { innerPadding ->
            BoxWithConstraints(modifier = Modifier.padding(innerPadding)) {
                val screenHeight = maxHeight
                val screenWidth = maxWidth
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(state = scrollState)
                ) {
                    Header(plan = uiState.plan, screenWidth = screenWidth)

                    Spacer(modifier = Modifier.height(16.dp))
                    ActionItemsTabs(
                        modifier = Modifier.height(screenHeight),
                        scrollState = scrollState,
                        steps = uiState.steps,
                        budgets = uiState.budgets,
                        navigateToStepDetails = navigateToStepDetails,
                        navigateToSaveStep = navigateToSaveStep
                    )
                }
            }
        }
    }


    @Composable
    private fun Header(plan: Plan, screenWidth: Dp) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(horizontal = horizontalScreenPadding)) {
                Avatar(
                    imageUrl = plan.avatar.value,
                    size = DpSize(80.dp, 80.dp),
                    shape = Shapes().large
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = plan.sector,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W400
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            PlanSummary(
                planDuration = plan.duration,
                targetAmount = plan.targetAmount,
                amountRaised = plan.totalFundsRaised,
                screenWidth = screenWidth
            )

            Spacer(modifier = Modifier.height(16.dp))
            BodyText(
                text = plan.description,
                modifier = Modifier.padding(horizontal = horizontalScreenPadding)
            )
        }
    }

    @Composable
    private fun PlanSummary(
        planDuration: TaskDuration,
        targetAmount: Amount,
        amountRaised: Amount,
        screenWidth: Dp,
    ) {
        @Composable
        fun Item(
            imagePainter: Painter,
            text: String,
            contentDescription: String? = null,
            modifier: Modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Icon(
                    painter = imagePainter,
                    contentDescription = contentDescription
                )

                Spacer(Modifier.height(4.dp))
                Text(text = text, style = MaterialTheme.typography.labelLarge)
            }
        }

        val dividerWidth = 1.dp
        val horizontalPadding = 8.dp
        val itemWidth = remember { (screenWidth - (dividerWidth * 2 + horizontalPadding * 2)) / 3 }
        val dividerModifier = Modifier.fillMaxHeight(.5f).width(dividerWidth)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding)
        ) {

            Item(
                imagePainter = painterResource(SharedRes.images.ic_calendar),
                text = stringResource(SharedRes.strings.x_months, planDuration.value),
                modifier = Modifier.width(itemWidth)
            )

            HorizontalDivider(modifier = dividerModifier)
            Item(
                imagePainter = painterResource(SharedRes.images.ic_target_arrow),
                text = targetAmount.getFormattedValue(),
                modifier = Modifier.width(itemWidth)
            )

            HorizontalDivider(modifier = dividerModifier)
            Item(
                imagePainter = painterResource(SharedRes.images.ic_rising_arrow),
                text = amountRaised.getFormattedValue(),
                modifier = Modifier.width(itemWidth)
            )
        }
    }

    @Composable
    private fun ActionItemsTabs(
        modifier: Modifier,
        scrollState: ScrollState,
        steps: Steps,
        budgets: Budgets,
        navigateToStepDetails: (Step) -> Unit,
        navigateToSaveStep: () -> Unit
    ) {
        val list = listOf(
            stringResource(SharedRes.strings.steps),
            stringResource(SharedRes.strings.budgets)
        )

        val pagerState = rememberPagerState(initialPage = 0) { list.size }
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = modifier) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {},
                modifier = Modifier.fillMaxWidth(),
                indicator = { _ -> }
            ) {
                list.forEachIndexed { index, text ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = text, color = Color(0xff6FAAEE)) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxHeight()
                    .nestedScroll(
                        remember {
                            object : NestedScrollConnection {
                                override fun onPreScroll(
                                    available: Offset,
                                    source: NestedScrollSource
                                ): Offset {
                                    return if (available.y > 0) Offset.Zero else Offset(
                                        x = 0f,
                                        y = -scrollState.dispatchRawDelta(-available.y)
                                    )
                                }
                            }
                        })
            ) { page: Int ->
                val listModifier = Modifier.fillMaxSize()
                    .padding(horizontal = 12.dp)

                when {
                    steps.isEmpty() -> EmptyListView(navigateToSaveStep = navigateToSaveStep)
                    page == 0 -> {
                        StepList(
                            modifier = listModifier,
                            steps = steps,
                            navigateToStepDetails = navigateToStepDetails
                        )
                    }

                    page == 1 -> BudgetList(modifier = listModifier, budgets = budgets)
                }
            }
        }
    }

    @Composable
    private fun StepItem(
        modifier: Modifier,
        name: String,
        targetAmount: String,
        amountRaised: String,
        onClick: () -> Unit
    ) {
        @Composable
        fun AmountItem(label: String, value: String) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, style = MaterialTheme.typography.labelMedium)
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
            }
        }

        Card(
            shape = Shapes().large,
            onClick = onClick,
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1)
            )
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))
                AmountItem(
                    label = stringResource(SharedRes.strings.target_funds_label),
                    value = targetAmount
                )

                AmountItem(
                    label = stringResource(SharedRes.strings.total_funds_raised),
                    value = amountRaised
                )
            }
        }
    }

    @Composable
    private fun StepList(modifier: Modifier, steps: Steps, navigateToStepDetails: (Step) -> Unit) {
        val itemModifier = Modifier.fillMaxWidth()
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(steps) {
                StepItem(
                    modifier = itemModifier,
                    name = it.name,
                    targetAmount = it.targetFunds.getFormattedValue(),
                    amountRaised = it.totalFundsRaised.getFormattedValue(),
                    onClick = { navigateToStepDetails(it) }
                )
            }
        }
    }

    @Composable
    private fun BudgetList(modifier: Modifier, budgets: Budgets) {
        val itemModifier = Modifier.fillMaxWidth()
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(budgets) {
                BudgetItem(
                    modifier = itemModifier,
                    name = it.name,
                    targetAmount = it.cost,
                    amountRaised = it.fundsRaised
                )
            }
        }
    }

    @Composable
    private fun EmptyListView(modifier: Modifier = Modifier, navigateToSaveStep: () -> Unit) {
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(SharedRes.strings.you_currently_have_no_step),
                style = MaterialTheme.typography.bodyLarge
            )

            TextButton(onClick = navigateToSaveStep) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(SharedRes.strings.add_step))
                }
            }
        }
    }

    @Composable
    private fun DropDownMenu(
        navigateToFundRequest: () -> Unit,
        navigateToEditPlan: () -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "more"
                )
            }

            DropdownMenu(
                modifier = Modifier.width(IntrinsicSize.Max),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(SharedRes.strings.edit_plan)) },
                    onClick = navigateToEditPlan
                )

                DropdownMenuItem(
                    text = { Text(text = stringResource(SharedRes.strings.delete_plan)) },
                    onClick = { }
                )

                DropdownMenuItem(
                    text = { Text(text = stringResource(SharedRes.strings.request_funds)) },
                    onClick = navigateToFundRequest
                )
            }
        }
    }

    @Composable
    private fun PlanDetailsExtendedFab(onClick: () -> Unit) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            text = { Text(text = stringResource(SharedRes.strings.add_step)) },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
        )
    }

    @Composable
    private fun PlanDetailsAppBar(
        onNavClick: () -> Unit,
        navigateToFundRequest: () -> Unit,
        navigateToEditPlan: () -> Unit
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onNavClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "navigation icon"
                    )
                }
            },
            title = {},
            actions = {
                DropDownMenu(
                    navigateToFundRequest = navigateToFundRequest,
                    navigateToEditPlan = navigateToEditPlan
                )
            }
        )
    }
}
