package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.account

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SponsorAccountTabViewModel(userRepository: UserRepository) : ScreenModel {

    val walletBalance = userRepository.userFlow.map { it?.walletBalance?.getFormattedValue() }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = userRepository.userFlow.value?.walletBalance?.getFormattedValue()
        )
}