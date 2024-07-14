package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.completestep

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CompleteStepViewModel(
    stepID: ID,
    budgetRepository: BudgetRepository
) : ScreenModel {


    val state: StateFlow<CompleteStepUiState> = budgetRepository.getBudgetsForStepAsFlow(stepID)
        .map { CompleteStepUiState.Content(it.toBudgetItems()) }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CompleteStepUiState.Loading
        )


    fun onEvent(event: CompleteStepUiEvent) {
        when (event) {
            CompleteStepUiEvent.CompleteStep -> completeStep()
        }
    }

    private fun completeStep() {

    }

    private fun List<Budget>.toBudgetItems(): ImmutableList<BudgetItem> {
        return map { budget ->
            BudgetItem(
                id = budget.id,
                name = budget.name,
                amount = budget.cost.getFormattedValue(),
                uploadStatus = "No upload"
            )
        }.toImmutableList()
    }
}