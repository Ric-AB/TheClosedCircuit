package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan

import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanOption

sealed interface CreatePlanUiEvent {
    data class PlanImageChange(val bytes: ByteArray): CreatePlanUiEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as PlanImageChange

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    data class CategoryChange(val category: PlanOption) : CreatePlanUiEvent
    data class SectorChange(val sector: PlanOption) : CreatePlanUiEvent
    data class BusinessType(val businessType: PlanOption) : CreatePlanUiEvent
    data class NameChange(val name: String) : CreatePlanUiEvent
    data class DescriptionChange(val description: String) : CreatePlanUiEvent
    data class DurationChange(val duration: String) : CreatePlanUiEvent
    data class SellingPriceChange(val price: String) : CreatePlanUiEvent
    data class CostPriceChange(val price: String) : CreatePlanUiEvent
    data class InputFieldFocusReceived(val fieldName: String) : CreatePlanUiEvent
    object InputFieldFocusLost : CreatePlanUiEvent
    object Submit : CreatePlanUiEvent
}