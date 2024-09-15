package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.collections.immutable.ImmutableList

sealed interface StepApprovalUiState {
    object Loading : StepApprovalUiState

    data class Content(
        val beneficiaryId: ID,
        val loading: Boolean,
        val loadingUser: Boolean,
        val canApproveStep: Boolean,
        val canApproveBudget: Boolean,
        val stepApprovalEnabled: Boolean,
        val proofItems: ImmutableList<ProofItem>
    ) : StepApprovalUiState

    data class Error(val message: String) : StepApprovalUiState

    val postLoading: Boolean
        get() {
            return this is Content && this.loading
        }
}

data class ProofItem(
    val id: ID,
    val name: String,
    val description: String,
    val isApproved: Boolean,
    val images: ImmutableList<String>,
)

sealed interface StepApprovalResult {
    data class ChatUserSuccess(val chatUser: ChatUser) : StepApprovalResult
    object ApproveBudgetSuccess : StepApprovalResult

    object ApproveStepSuccess : StepApprovalResult

    data class Error(val message: String) : StepApprovalResult
}