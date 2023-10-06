package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.editplan

import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption

sealed interface EditPlanUIEvent {
    data class SectorChange(val sector: PlanOption) : EditPlanUIEvent
    data class BusinessType(val businessType: PlanOption) : EditPlanUIEvent
    data class NameChange(val name: String) : EditPlanUIEvent
    data class DescriptionChange(val description: String) : EditPlanUIEvent
    data class DurationChange(val duration: String) : EditPlanUIEvent
    data class SellingPriceChange(val price: String) : EditPlanUIEvent
    data class CostPriceChange(val price: String) : EditPlanUIEvent
    data class InputFieldFocusReceived(val fieldName: String) : EditPlanUIEvent
    object InputFieldFocusLost : EditPlanUIEvent
    object Submit : EditPlanUIEvent
}