package com.closedcircuit.closedcircuitapplication.sponsor.data.offer

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferPayload
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferResponse
import com.closedcircuit.closedcircuitapplication.sponsor.domain.offer.OfferRepository

class OfferRepositoryImpl(private val service: OfferService) : OfferRepository {

    override suspend fun sendOffer(offerPayload: OfferPayload): ApiResponse<OfferResponse> {
        return service.sendOffer(offerPayload)
    }
}