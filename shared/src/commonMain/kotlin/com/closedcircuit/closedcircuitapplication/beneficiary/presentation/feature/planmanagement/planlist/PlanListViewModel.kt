package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlanListViewModel(planRepository: PlanRepository) : ScreenModel {

    val stateFlow = planRepository.getPlansAsFlow()
        .map { PlanListUiState(it) }
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            PlanListUiState(persistentListOf())
        )
}