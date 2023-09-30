package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.util.InputField
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class CreatePlanViewModel(private val planRepository: PlanRepository) : ScreenModel {

    var state by mutableStateOf(CreatePlanUIState(nameField = InputField("WIINSN")))

    private val _createPlanResultChannel = Channel<CreatePlanResult>()
    val createPlanResultChannel: ReceiveChannel<CreatePlanResult> = _createPlanResultChannel

    private var lastFocusedField: String? = null

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
            is CreatePlanUIEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            CreatePlanUIEvent.InputFieldFocusLost -> validateLastFocusedField()
            CreatePlanUIEvent.Submit -> attemptPlanCreation()
        }
    }

    private fun attemptPlanCreation() {
        if (areFieldsValid()) {
            state = state.copy(isLoading = true)
            coroutineScope.launch {
                planRepository.createPlan(buildPlan()).onSuccess {
                    _createPlanResultChannel.send(CreatePlanResult.Success)
                }.onError { _, message ->
                    _createPlanResultChannel.send(CreatePlanResult.Failure(message))
                }
            }
        }
    }

    private fun buildPlan(): Plan {
        val defaultSectorAndType = "others"
        return Plan.buildPlan(
            category = state.category?.id.orEmpty(),
            sector = state.sector?.id ?: defaultSectorAndType,
            type = state.businessType?.id ?: defaultSectorAndType,
            name = state.nameField.value,
            description = state.descriptionField.value,
            duration = TaskDuration(state.durationField.value.toInt()),
            estimatedSellingPrice = Price(state.estimatedSellingPriceField.value.toDouble()),
            estimatedCostPrice = Price(state.estimatedCostPriceField.value.toDouble())
        )
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

    private fun areFieldsValid(): Boolean {
        return state.fieldsToValidate.let { fields ->
            fields.forEach { it.validateInput() }
            fields.all { !it.isError }
        }
    }

    private fun validateLastFocusedField() {
        val fieldToValidate = state.fieldsToValidate.find { it.name == lastFocusedField }
        fieldToValidate?.validateInput()
    }

    private fun updateLastFocusedField(fieldName: String) {
        lastFocusedField = fieldName
    }
}//