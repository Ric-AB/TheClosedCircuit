package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption

class CreatePlanViewModel : ScreenModel {

    var state by mutableStateOf(CreatePlanUIState())

    fun onEvent(event: CreatePlanUIEvent) {
        when (event) {
            is CreatePlanUIEvent.BusinessType -> updateBusinessType(event.businessType)
            is CreatePlanUIEvent.CategoryChange -> updateCategory(event.category)
            is CreatePlanUIEvent.CostPriceChange -> updateCostPrice(event.price)
            is CreatePlanUIEvent.DurationChange -> updateDuration(event.duration)
            is CreatePlanUIEvent.DescriptionChange -> updateDescription(event.description)
            is CreatePlanUIEvent.NameChange -> updateName(event.name)
            is CreatePlanUIEvent.SectorChange -> updateSector(event.sector)
            is CreatePlanUIEvent.SellingPriceChange -> updateSellingPrice(event.price)
            CreatePlanUIEvent.Submit -> attemptPlanCreation()
        }
    }

    private fun attemptPlanCreation() {

    }

    private fun updateBusinessType(businessType: PlanOption) {
        state = state.copy(businessType = businessType)
    }

    private fun updateCategory(category: PlanOption) {
        state = state.copy(category = category)
    }

    private fun updateCostPrice(price: String) {
        state.estimatedCostPriceField.onValueChange(price)
    }

    private fun updateDuration(duration: String) {
        state.durationField.onValueChange(duration)
    }

    private fun updateDescription(description: String) {
        state.descriptionField.onValueChange(description)
    }

    private fun updateName(name: String) {
        state.nameField.onValueChange(name)
    }

    private fun updateSector(sector: PlanOption) {
        state = state.copy(sector = sector)
    }

    private fun updateSellingPrice(price: String) {
        state.estimatedSellingPriceField.onValueChange(price)
    }
}