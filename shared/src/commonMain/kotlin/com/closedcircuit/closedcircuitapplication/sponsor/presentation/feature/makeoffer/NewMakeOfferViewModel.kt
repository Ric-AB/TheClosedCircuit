package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import cafe.adriel.voyager.core.model.ScreenModel
import com.closedcircuit.closedcircuitapplication.sponsor.domain.model.FundingLevel
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest

class NewMakeOfferViewModel : ScreenModel {

    private val planToSponsor = MutableStateFlow<SponsorPlan?>(null)
//    val planSummaryUiState = planToSponsor.
    private val fundingLevel = MutableStateFlow(FundingLevel.PLAN)
    private val selectableFundingItem = fundingLevel.mapLatest {

    }

    private val _makeOfferResultChannel = Channel<MakeOfferResult>()
    val makeOfferResultChannel: ReceiveChannel<MakeOfferResult> = _makeOfferResultChannel
}