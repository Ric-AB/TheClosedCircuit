package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID

sealed interface StepApprovalUiEvent {

    data class ApproveBudget(val id: ID) : StepApprovalUiEvent
    object ApproveStep : StepApprovalUiEvent
}