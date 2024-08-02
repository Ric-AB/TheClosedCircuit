package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    userRepository: UserRepository,
    planRepository: PlanRepository
) : ScreenModel {

    private val userFlow = userRepository.userFlow
    private val plansFlow = planRepository.getPlansAsFlow()

    val state: StateFlow<ProfileUIState?> = combine(userFlow, plansFlow) { user, _ ->
        ProfileUIState.init(user)
    }.stateIn(
        screenModelScope,
        SharingStarted.WhileSubscribed(),
        ProfileUIState.init(userFlow.value)
    )
}