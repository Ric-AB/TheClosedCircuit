package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails

import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.replaceAll
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlanDetailsViewModel(
    private val plan: Plan,
    private val planRepository: PlanRepository,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository
) : BaseScreenModel<PlanDetailsUiState, PlanDetailsResult>() {

    private val stepsCopy = mutableListOf<Step>()
    private val budgetsCopy = mutableListOf<Budget>()
    private val isLoadingFlow = MutableStateFlow(false)
    private val planID = plan.id
    val state = combine(
        planRepository.getPlanByIDAsFlow(planID),
        stepRepository.getStepsForPlanAsFlow(planID),
        budgetRepository.getBudgetsForPlanAsFlow(planID),
        isLoadingFlow
    ) { plan, steps, budgets, isLoading ->
        val nonNullPlan = plan ?: this.plan
        if (steps.isNotEmpty()) stepsCopy.replaceAll(steps)
        if (budgets.isNotEmpty()) budgetsCopy.replaceAll(budgets)

        PlanDetailsUiState(
            isLoading = isLoading,
            canEditPlan = !nonNullPlan.hasReceivedFunds(),
            plan = nonNullPlan,
            steps = stepsCopy,
            budgets = budgetsCopy
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PlanDetailsUiState.init(plan)
    )

    fun onEvent(event: PlanDetailsUiEvent) {
        when (event) {
            PlanDetailsUiEvent.Delete -> deletePlan()
        }
    }

    private fun deletePlan() {
        screenModelScope.launch {
            isLoadingFlow.emit(true)
            planRepository.deletePlan(planID).onSuccess {
                stepRepository.deleteStepsLocally(state.value.steps)
                budgetRepository.deleteBudgetsLocally(state.value.budgets)
                handleSuccess()
            }.onError { _, message ->
                handleError(message)
            }
        }
    }

    private suspend fun handleSuccess() {
        _resultChannel.send(PlanDetailsResult.DeleteSuccess)
        isLoadingFlow.emit(false)
    }

    private suspend fun handleError(message: String) {
        _resultChannel.send(PlanDetailsResult.DeleteError(message))
        isLoadingFlow.emit(false)
    }
}