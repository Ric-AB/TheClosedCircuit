package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.StepRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class StepApprovalViewModel(
    private val stepID: ID,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository
) : BaseScreenModel<StepApprovalUiState, StepApprovalResult>() {

    val state: MutableState<StepApprovalUiState> = mutableStateOf(StepApprovalUiState.Loading)

    init {
        fetchStepProofs()
    }

    fun onEvent(event: StepApprovalUiEvent) {
        when (event) {
            is StepApprovalUiEvent.ApproveBudget -> approveBudget(event.id)
            StepApprovalUiEvent.ApproveStep -> approveStep()
        }
    }

    private fun fetchStepProofs() {
        screenModelScope.launch {
            stepRepository.fetchStepProofs(stepID).onSuccess { proofs ->
                state.value = StepApprovalUiState.Content(
                    loading = false,
                    proofItems = proofs.map { proof ->
                        val firstDoc = proof.files.first()
                        ProofItem(
                            id = proof.id,
                            name = firstDoc.title,
                            description = firstDoc.description,
                            isApproved = false,
                            images = proof.files.map { it.url.value }.toImmutableList()
                        )
                    }.toImmutableList()
                )
            }.onError { _, message ->
                state.value = StepApprovalUiState.Error(message)
            }
        }
    }

    private fun approveBudget(id: ID) {
        screenModelScope.launch {
            val loadedState = (state.value as? StepApprovalUiState.Content) ?: return@launch
            state.value = loadedState.copy(loading = true)
            budgetRepository.approveBudget(id).onSuccess {
                val proofItemItems = loadedState.proofItems
                val updateProofItems = proofItemItems.map {
                    if (it.id == id) it.copy(isApproved = true)
                    else it
                }.toImmutableList()

                _resultChannel.send(StepApprovalResult.ApproveBudgetSuccess)
                state.value = loadedState.copy(loading = false, proofItems = updateProofItems)
            }.onError { _, message ->
                _resultChannel.send(StepApprovalResult.Error(message))
                state.value = loadedState.copy(loading = false)
            }
        }
    }

    private fun approveStep() {

    }
}