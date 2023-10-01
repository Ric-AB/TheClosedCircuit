@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails

import StepDetailsScreen
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberCalendarMonth
import com.closedcircuit.closedcircuitapplication.presentation.feature.fundrequest.FundRequestScreen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal data class PlanDetailsScreen(val plan: Plan) : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    private val viewModel: PlanDetailsViewModel by inject { parametersOf(plan) }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = viewModel.state
        ScreenContent(
            uiState = uiState,
            goBack = navigator::pop,
            navigateToStepDetails = { navigator.push(StepDetailsScreen) },
            navigateToFundRequest = { navigator.push(FundRequestScreen) }
        )
    }
}

@Composable
private fun ScreenContent(
    uiState: PlanDetailsUIState,
    goBack: () -> Unit,
    navigateToStepDetails: () -> Unit,
    navigateToFundRequest: () -> Unit
) {
    BaseScaffold(topBar = {
        PlanDetailsAppBar(
            onNavClick = goBack,
            navigateToFundRequest = navigateToFundRequest
        )
    }) { innerPadding ->
        BoxWithConstraints(modifier = Modifier.padding(innerPadding)) {
            val screenHeight = maxHeight
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                Header(plan = uiState.plan)

                Spacer(modifier = Modifier.height(16.dp))
                ActionItemsTabs(
                    modifier = Modifier.height(screenHeight),
                    scrollState = scrollState,
                    navigateToStepDetails = navigateToStepDetails
                )
            }
        }
    }
}


@Composable
private fun Header(plan: Plan) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)) {
            Avatar(
                avatar = plan.avatar,
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
            amountRaised = plan.fundsRaised,
        )

        Spacer(modifier = Modifier.height(16.dp))
        BodyText(
            text = plan.description,
            modifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)
        )
    }
}

@Composable
private fun PlanSummary(
    planDuration: TaskDuration,
    targetAmount: Price,
    amountRaised: Price,
) {
    @Composable
    fun Item(imageVector: ImageVector, text: String, contentDescription: String? = null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(100.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription
            )

            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {
        val dividerModifier = Modifier.fillMaxHeight(.5f).width(1.dp)
        Item(
            imageVector = rememberCalendarMonth(),
            text = stringResource(SharedRes.strings.x_months, planDuration.value)
        )

        Divider(modifier = dividerModifier)

        Item(
            imageVector = rememberCalendarMonth(),
            text = "NGN ${targetAmount.value}"
        )

        Divider(modifier = dividerModifier)

        Item(
            imageVector = rememberCalendarMonth(),
            text = "NGN ${amountRaised.value}"
        )
    }
}

@Composable
private fun ActionItemsTabs(
    modifier: Modifier,
    scrollState: ScrollState,
    navigateToStepDetails: () -> Unit
) {
    val list = listOf("Steps", "Budgets")
    val pagerState = rememberPagerState(initialPage = 0) { list.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {},
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions -> }
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

            when (page) {
                0 -> StepList(
                    modifier = listModifier,
                    navigateToStepDetails = navigateToStepDetails
                )

                1 -> BudgetList(modifier = listModifier)
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
            AmountItem(label = "Target funds", value = targetAmount)
            AmountItem(label = "Total funds raised", value = amountRaised)
        }
    }
}

@Composable
private fun StepList(modifier: Modifier, navigateToStepDetails: () -> Unit) {
    val itemModifier = Modifier.fillMaxWidth()
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(10) {
            StepItem(
                modifier = itemModifier,
                name = "Conduct Market Research",
                targetAmount = "NGN 500",
                amountRaised = "NGN 200",
                onClick = navigateToStepDetails
            )
        }
    }
}

@Composable
private fun BudgetList(modifier: Modifier) {
    val itemModifier = Modifier.fillMaxWidth()
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(10) {
            BudgetItem(
                modifier = itemModifier,
                name = "Website hosting fee",
                targetAmount = "NGN 20,000",
                amountRaised = "NGN 14,000"
            )
        }
    }
}

@Composable
private fun DropDownMenu(navigateToFundRequest: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Edit Plan") },
                onClick = { }
            )

            DropdownMenuItem(
                text = { Text("Delete Plan") },
                onClick = { }
            )

            DropdownMenuItem(
                text = { Text("Request Funds") },
                onClick = navigateToFundRequest
            )
        }
    }
}

@Composable
private fun PlanDetailsAppBar(onNavClick: () -> Unit, navigateToFundRequest: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "navigation icon")
            }
        },
        title = {},
        actions = {
            DropDownMenu(navigateToFundRequest)
        }
    )
}