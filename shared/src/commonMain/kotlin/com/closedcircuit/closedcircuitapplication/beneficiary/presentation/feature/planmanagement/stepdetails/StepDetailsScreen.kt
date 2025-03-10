@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.completestep.CompleteStepScreen
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep.SaveStepScreen
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.presentation.component.AppAlertDialog
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.icon.rememberTask
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

internal data class StepDetailsScreen(val step: Step) : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<StepDetailsViewModel> { parametersOf(step) }
        val messageBarState = rememberMessageBarState()
        val uiState by viewModel.state.collectAsState()
        var showDeleteDialog by remember { mutableStateOf(false) }

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is StepDetailsResult.DeleteError -> messageBarState.addError(it.message)
                StepDetailsResult.DeleteSuccess ->
                    messageBarState.addSuccess("Deleted successfully.") {
                        navigator.pop()
                    }
            }
        }

        ScreenContent(
            uiState = uiState,
            messageBarState = messageBarState,
            showDeleteDialog = showDeleteDialog,
            goBack = navigator::pop,
            toggleDeleteDialog = { showDeleteDialog = it },
            onEvent = viewModel::onEvent,
            navigateToSaveStep = {
                val step = uiState.step
                navigator.push(
                    SaveStepScreen(planId = step.planID, step = step)
                )
            },
            navigateToCompleteStep = {
                navigator.push(
                    CompleteStepScreen(
                        planID = step.planID,
                        stepID = step.id
                    )
                )
            }
        )
    }

    @Composable
    private fun ScreenContent(
        uiState: StepDetailsUiState,
        messageBarState: MessageBarState,
        showDeleteDialog: Boolean,
        toggleDeleteDialog: (Boolean) -> Unit,
        goBack: () -> Unit,
        onEvent: (StepDetailsUiEvent) -> Unit,
        navigateToSaveStep: () -> Unit,
        navigateToCompleteStep: (Step) -> Unit
    ) {
        BaseScaffold(
            topBar = {
                StepDetailsAppBar(
                    canCompleteStep = uiState.canCompleteStep,
                    canDeleteStep = uiState.canDeleteStep,
                    goBack = goBack,
                    toggleDeleteDialog = { toggleDeleteDialog(true) },
                    navigateToSaveStep = navigateToSaveStep,
                    navigateToCompleteStep = { navigateToCompleteStep(uiState.step) }
                )
            },
            showLoadingDialog = uiState.isLoading,
            messageBarState = messageBarState
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                val step = uiState.step
                Text(
                    text = step.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = horizontalScreenPadding)
                )

                Spacer(modifier = Modifier.height(16.dp))
                StepSummary(
                    stepDuration = step.duration,
                    targetAmount = step.targetFunds,
                    amountRaised = step.totalFundsRaised
                )

                Spacer(modifier = Modifier.height(16.dp))
                BodyText(
                    text = step.description,
                    modifier = Modifier.padding(horizontal = horizontalScreenPadding)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(SharedRes.strings.budget_items),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = horizontalScreenPadding)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.budgets.forEach {
                        BudgetItem(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                            name = it.name,
                            targetAmount = it.cost.getFormattedValue(),
                            amountRaised = it.fundsRaised.getFormattedValue(),
                            amountRaisedPercent = it.fundsRaisedPercent.toFloat()
                        )
                    }
                }
            }

            AppAlertDialog(
                visible = showDeleteDialog,
                onDismissRequest = { toggleDeleteDialog(false) },
                onConfirm = { onEvent(StepDetailsUiEvent.Delete) },
                confirmTitle = stringResource(SharedRes.strings.confirm_label),
                title = stringResource(SharedRes.strings.delete_step_label),
                text = stringResource(SharedRes.strings.delete_step_prompt_label)
            )
        }
    }

    @Composable
    private fun StepSummary(
        stepDuration: TaskDuration,
        targetAmount: Amount,
        amountRaised: Amount
    ) {
        @Composable
        fun Item(imagePainter: Painter, text: String, contentDescription: String? = null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(100.dp)
            ) {
                Icon(
                    painter = imagePainter,
                    contentDescription = contentDescription
                )

                Spacer(Modifier.height(8.dp))
                Text(text = text, style = MaterialTheme.typography.labelLarge)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 8.dp)
        ) {
            val dividerModifier = Modifier.fillMaxHeight(.5f).width(1.dp)
            Item(
                imagePainter = painterResource(SharedRes.images.ic_calendar),
                text = stringResource(SharedRes.strings.x_months, stepDuration.value)
            )

            HorizontalDivider(modifier = dividerModifier)
            Item(
                imagePainter = painterResource(SharedRes.images.ic_target_arrow),
                text = targetAmount.getFormattedValue()
            )

            HorizontalDivider(modifier = dividerModifier)
            Item(
                imagePainter = painterResource(SharedRes.images.ic_rising_arrow),
                text = amountRaised.getFormattedValue()
            )
        }
    }

    @Composable
    private fun StepDetailsAppBar(
        canCompleteStep: Boolean,
        canDeleteStep: Boolean,
        goBack: () -> Unit,
        toggleDeleteDialog: () -> Unit,
        navigateToSaveStep: () -> Unit,
        navigateToCompleteStep: () -> Unit
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "navigation icon"
                    )
                }
            },
            title = { },
            actions = {
                StepDetailsAppBarActions(
                    canCompleteStep = canCompleteStep,
                    canDeleteStep = canDeleteStep,
                    onDeleteIconClick = toggleDeleteDialog,
                    onEditIconClick = navigateToSaveStep,
                    onCompleteIconClick = navigateToCompleteStep
                )
            }
        )
    }

    @Composable
    private fun StepDetailsAppBarActions(
        canCompleteStep: Boolean,
        canDeleteStep: Boolean,
        onDeleteIconClick: () -> Unit,
        onEditIconClick: () -> Unit,
        onCompleteIconClick: () -> Unit
    ) {
        IconButton(onClick = onEditIconClick) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit step")
        }

        if (canCompleteStep) {
            IconButton(onClick = onCompleteIconClick) {
                Icon(imageVector = rememberTask(), contentDescription = "complete step")
            }
        }

        if (canDeleteStep) {
            DropDownMenu(onDeleteIconClick)
        }
    }

    @Composable
    private fun DropDownMenu(onDeleteIconClick: () -> Unit) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "more"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(SharedRes.strings.delete_step_label)) },
                    onClick = {
                        expanded = false
                        onDeleteIconClick()
                    }
                )
            }
        }
    }
}
