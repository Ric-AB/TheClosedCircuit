package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.step.StepRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlanDetailsViewModel(
    plan: Plan,
    planRepository: PlanRepository,
    stepRepository: StepRepository,
    budgetRepository: BudgetRepository
) : ScreenModel {

    private val planID = plan.id
    val state = combine(
        planRepository.getPlanByIDAsFlow(planID),
        stepRepository.getStepsForPlanAsFlow(planID),
        budgetRepository.getBudgetsForPlanAsFlow(planID)
    ) { plan, steps, budgets ->
        PlanDetailsUiState(
            plan = plan,
            steps = steps,
            budgets = budgets
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PlanDetailsUiState.init(plan)
    )
}