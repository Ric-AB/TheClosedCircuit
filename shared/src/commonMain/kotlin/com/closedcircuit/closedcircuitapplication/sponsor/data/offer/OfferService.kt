package com.closedcircuit.closedcircuitapplication.sponsor.data.offer

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferPayload
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface OfferService {

    @Headers("Content-Type: application/json")
    @POST(ClosedCircuitApiEndpoints.MAKE_OFFER)
    suspend fun sendOffer(@Body makeOfferRequest: OfferPayload): ApiResponse<OfferResponse>
}