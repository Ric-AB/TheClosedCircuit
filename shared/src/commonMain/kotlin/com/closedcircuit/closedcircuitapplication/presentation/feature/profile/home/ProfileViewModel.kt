package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    userRepository: UserRepository,
    planRepository: PlanRepository
) : ScreenModel {

    private val userFlow = userRepository.userFlow
    private val plansFlow = planRepository.plansFlow

    val state: StateFlow<ProfileUIState?> = combine(userFlow, plansFlow) { user, plans ->
        ProfileUIState.init(user)
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(),
        ProfileUIState.init(userFlow.value)
    )
}