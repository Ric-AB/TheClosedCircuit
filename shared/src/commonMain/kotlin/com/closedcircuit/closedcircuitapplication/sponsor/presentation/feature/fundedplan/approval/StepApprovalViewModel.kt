package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.StepRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class StepApprovalViewModel(
    private val stepID: ID,
    private val stepRepository: StepRepository
) : ScreenModel {

    val state: MutableState<StepApprovalUiState> = mutableStateOf(StepApprovalUiState.Loading)

    init {
        fetchStepProofs()
    }

    private fun fetchStepProofs() {
        screenModelScope.launch {
            stepRepository.fetchStepProofs(stepID).onSuccess { proofs ->
                state.value = StepApprovalUiState.Content(
                    proofItems = proofs.map { proof ->
                        val firstDoc = proof.documents.first()
                        ProofItem(
                            name = firstDoc.title,
                            description = firstDoc.description,
                            isApproved = false,
                            images = proof.documents.map { it.url.value }.toImmutableList()
                        )
                    }.toImmutableList()
                )
            }.onError { _, message ->
                state.value = StepApprovalUiState.Error(message)
            }
        }
    }
}