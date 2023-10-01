package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan

class PlanDetailsViewModel(
    plan: Plan
) : ScreenModel {

    val state by mutableStateOf(PlanDetailsUIState.init(plan))
}