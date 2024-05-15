package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

sealed interface MakeOfferEvent {
    object FetchPlan : MakeOfferEvent
    data class FundingLevelChange(val fundingLevel: FundingLevel) : MakeOfferEvent
}