package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails

import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.replaceAll
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StepDetailsViewModel(
    private val step: Step,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository
) : BaseScreenModel<StepDetailsUiState, StepDetailsResult>() {

    private val budgetsCopy = mutableListOf<Budget>()
    private val isLoadingFlow = MutableStateFlow(false)
    private val stepID = step.id
    val state = combine(
        stepRepository.getStepByIDAsFlow(stepID),
        stepRepository.getPrecedingStepAsFlowFor(stepID, step.planID),
        budgetRepository.getBudgetsForStepAsFlow(stepID),
        isLoadingFlow
    ) { step, precedingStep, budgets, isLoading ->
        val nonNullStep = step ?: this.step
        if (budgets.isNotEmpty()) {
            budgetsCopy.replaceAll(budgets)
        }

        StepDetailsUiState(
            isLoading = isLoading,
            step = nonNullStep,
            canCompleteStep = precedingStep?.isComplete ?: true,
            canDeleteStep = !nonNullStep.hasReceivedFunds,
            budgets = budgetsCopy.toImmutableList()
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StepDetailsUiState.init(step)
    )

    fun onEvent(event: StepDetailsUiEvent) {
        when (event) {
            StepDetailsUiEvent.Delete -> deleteStep()
        }
    }

    private fun deleteStep() {
        screenModelScope.launch {
            isLoadingFlow.emit(true)
            stepRepository.deleteStep(stepID).onSuccess {
                budgetRepository.deleteBudgetsLocally(state.value.budgets)
                handleSuccess()
            }.onError { _, message ->
                handleError(message)
            }
        }
    }

    private suspend fun handleSuccess() {
        _resultChannel.send(StepDetailsResult.DeleteSuccess)
        isLoadingFlow.emit(false)
    }

    private suspend fun handleError(message: String) {
        _resultChannel.send(StepDetailsResult.DeleteError(message))
        isLoadingFlow.emit(false)
    }
}