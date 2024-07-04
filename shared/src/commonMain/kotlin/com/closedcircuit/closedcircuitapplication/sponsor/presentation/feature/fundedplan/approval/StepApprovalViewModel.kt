package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.FundedBudget
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.StepRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class StepApprovalViewModel(
    private val planID: ID,
    private val stepID: ID,
    private val canApprove: Boolean,
    private val isStepApprovedByUser: Boolean,
    private val planRepository: PlanRepository,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository,
    private val userRepository: UserRepository
) : BaseScreenModel<StepApprovalUiState, StepApprovalResult>() {

    val state: MutableState<StepApprovalUiState> = mutableStateOf(StepApprovalUiState.Loading)

    init {
        fetchPlan()
    }

    fun onEvent(event: StepApprovalUiEvent) {
        when (event) {
            is StepApprovalUiEvent.ApproveBudget -> approveBudget(event.id)
            StepApprovalUiEvent.ApproveStep -> approveStep()
        }
    }

    private fun fetchPlan() {
        screenModelScope.launch {
            planRepository.fetchFundedPlanDetails(planID).onSuccess { fundedPlan ->
                val budgetsForStep = fundedPlan.steps.flatMap { it.budgets }
                    .filter { it.stepId == stepID }

                fetchStepProofs(budgetsForStep)
            }
        }
    }

    private suspend fun fetchStepProofs(budgetsForStep: List<FundedBudget>) {
        stepRepository.fetchStepProofs(stepID).onSuccess { proofs ->
            val userId = userRepository.userFlow.firstOrNull()?.id ?: return@onSuccess
            val proofItems = proofs.map { proof ->
                val firstDoc = proof.files.first()
                val isApproved = budgetsForStep.find { it.id == proof.id }
                    ?.approverIds?.any { it == userId }
                    .orFalse()

                ProofItem(
                    id = proof.id,
                    name = firstDoc.title,
                    description = firstDoc.description,
                    isApproved = isApproved,
                    images = proof.files.map { it.url.value }.toImmutableList()
                )
            }.toImmutableList()

            state.value = StepApprovalUiState.Content(
                loading = false,
                canApproveBudget = canApprove,
                canApproveStep = canApprove && !isStepApprovedByUser,
                stepApprovalEnabled = proofItems.isAllApproved(),
                proofItems = proofItems
            )
        }.onError { _, message ->
            state.value = StepApprovalUiState.Error(message)
        }
    }

    private fun approveBudget(id: ID) {
        screenModelScope.launch {
            val loadedState = loadedState() ?: return@launch
            state.value = loadedState.copy(loading = true)
            budgetRepository.approveBudget(id).onSuccess {
                val proofItemItems = loadedState.proofItems
                val updateProofItems = proofItemItems.map {
                    if (it.id == id) it.copy(isApproved = true)
                    else it
                }.toImmutableList()

                _resultChannel.send(StepApprovalResult.ApproveBudgetSuccess)
                state.value = loadedState.copy(
                    loading = false,
                    proofItems = updateProofItems,
                    stepApprovalEnabled = updateProofItems.isAllApproved()
                )
            }.onError { _, message ->
                _resultChannel.send(StepApprovalResult.Error(message))
                state.value = loadedState.copy(loading = false)
            }
        }
    }

    private fun approveStep() {
        screenModelScope.launch {
            val loadedState = loadedState() ?: return@launch
            state.value = loadedState.copy(loading = true)
            stepRepository.approveStep(stepID).onSuccess {
                _resultChannel.send(StepApprovalResult.ApproveStepSuccess)
                state.value = loadedState.copy(loading = false, stepApprovalEnabled = false)
            }.onError { _, message ->
                _resultChannel.send(StepApprovalResult.Error(message))
                state.value = loadedState.copy(loading = false)
            }
        }
    }

    private fun List<ProofItem>.isAllApproved() = this.all { it.isApproved }

    private fun loadedState(): StepApprovalUiState.Content? {
        return (state.value as? StepApprovalUiState.Content)
    }
}