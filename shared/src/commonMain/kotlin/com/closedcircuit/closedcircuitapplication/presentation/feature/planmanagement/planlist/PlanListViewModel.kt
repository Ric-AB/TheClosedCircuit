package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.planlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlanListViewModel(planRepository: PlanRepository) : ScreenModel {

    val stateFlow = planRepository.plansFlow
        .map { PlanListUiState(it) }
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000L),
            PlanListUiState(persistentListOf())
        )
}