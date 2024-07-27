package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails

sealed interface PlanDetailsUiEvent {
    object Delete : PlanDetailsUiEvent
}