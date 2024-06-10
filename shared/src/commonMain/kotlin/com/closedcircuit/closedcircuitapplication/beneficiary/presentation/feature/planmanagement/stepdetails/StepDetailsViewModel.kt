package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StepDetailsViewModel(
    step: Step,
    stepRepository: StepRepository,
    budgetRepository: BudgetRepository
) : ScreenModel {

    private val stepID = step.id
    val state = combine(
        stepRepository.getStepByIDAsFlow(stepID),
        budgetRepository.getBudgetsForStepAsFlow(stepID)
    ) { step, budgets ->
        StepDetailsUiState(step = step, budgets = budgets)
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StepDetailsUiState.init(step)
    )
}