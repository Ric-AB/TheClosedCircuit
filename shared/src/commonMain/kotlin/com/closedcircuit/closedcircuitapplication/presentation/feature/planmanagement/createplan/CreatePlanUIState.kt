package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.util.InputField
import kotlinx.collections.immutable.ImmutableList

data class CreatePlanUIState(
    val category: PlanOption? = null,
    val sector: PlanOption? = null,
    val businessType: PlanOption? = null,
    val nameField: InputField = InputField(),
    val descriptionField: InputField = InputField(),
    val durationField: InputField = InputField(),
    val estimatedSellingPriceField: InputField = InputField(),
    val estimatedCostPriceField: InputField = InputField(),
    val categories: ImmutableList<PlanOption> = PlanOption.getCategories(),
    val sectors: ImmutableList<PlanOption> = PlanOption.getSectors(),
    val businessTypes: ImmutableList<PlanOption> = PlanOption.getTypes()
)
