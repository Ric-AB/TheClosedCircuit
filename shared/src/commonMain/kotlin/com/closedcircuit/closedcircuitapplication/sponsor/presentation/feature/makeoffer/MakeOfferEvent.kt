package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.sponsor.domain.model.FundingLevel

sealed interface MakeOfferEvent {
    data class FundingLevelChange(val fundingLevel: FundingLevel) : MakeOfferEvent
    data class FundTypeChange(val fundType: FundType) : MakeOfferEvent
    data class ToggleFundingItem(val index: Int) : MakeOfferEvent
    data class EnterAmount(val amount: String) : MakeOfferEvent
    object ToggleAllFundingItems : MakeOfferEvent
    object CreateSchedule : MakeOfferEvent
    object FetchChatUser : MakeOfferEvent
    object SubmitOffer : MakeOfferEvent
}