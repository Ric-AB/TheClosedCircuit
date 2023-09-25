package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption

sealed interface CreatePlanUIEvent {
    data class CategoryChange(val category: PlanOption) : CreatePlanUIEvent
    data class SectorChange(val sector: PlanOption) : CreatePlanUIEvent
    data class BusinessType(val businessType: PlanOption) : CreatePlanUIEvent
    data class NameChange(val name: String) : CreatePlanUIEvent
    data class DescriptionChange(val description: String) : CreatePlanUIEvent
    data class DurationChange(val duration: String) : CreatePlanUIEvent
    data class SellingPriceChange(val price: String) : CreatePlanUIEvent
    data class CostPriceChange(val price: String) : CreatePlanUIEvent
    data class InputFieldFocusReceived(val fieldName: String) : CreatePlanUIEvent
    object InputFieldFocusLost : CreatePlanUIEvent
    object Submit : CreatePlanUIEvent
}