package com.closedcircuit.closedcircuitapplication.sponsor.domain.offer

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferPayload
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferResponse

interface OfferRepository {

    suspend fun sendOffer(offerPayload: OfferPayload): ApiResponse<OfferResponse>
}