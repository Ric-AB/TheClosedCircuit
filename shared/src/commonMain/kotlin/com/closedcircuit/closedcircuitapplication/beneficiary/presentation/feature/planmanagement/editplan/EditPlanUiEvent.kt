package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.editplan

import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanOption

sealed interface EditPlanUiEvent {
    data class SectorChange(val sector: PlanOption) : EditPlanUiEvent
    data class BusinessType(val businessType: PlanOption) : EditPlanUiEvent
    data class NameChange(val name: String) : EditPlanUiEvent
    data class DescriptionChange(val description: String) : EditPlanUiEvent
    data class DurationChange(val duration: String) : EditPlanUiEvent
    data class SellingPriceChange(val price: String) : EditPlanUiEvent
    data class CostPriceChange(val price: String) : EditPlanUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : EditPlanUiEvent
    object InputFieldFocusLost : EditPlanUiEvent
    object Submit : EditPlanUiEvent
}