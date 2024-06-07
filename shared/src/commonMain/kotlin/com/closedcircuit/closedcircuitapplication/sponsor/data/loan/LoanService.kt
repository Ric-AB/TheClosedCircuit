package com.closedcircuit.closedcircuitapplication.sponsor.data.loan

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.CANCEL_LOAN_OFFER
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto.ChangeLoanStatusPayload
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto.GetLoanOffersDto
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto.LoanOfferDetailsDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface LoanService {

    @GET(ClosedCircuitApiEndpoints.GET_SPONSOR_OFFERS)
    suspend fun fetchLoanOffers(@Query("status") status: String): ApiResponse<GetLoanOffersDto>

    @GET(ClosedCircuitApiEndpoints.GET_SPONSOR_OFFER_DETAILS)
    suspend fun fetchLoanOfferDetails(@Path("id") offerId: String): ApiResponse<LoanOfferDetailsDto>

    @PATCH(CANCEL_LOAN_OFFER)
    suspend fun cancelLoanOffer(@Path("id") offerId: String, @Body payload: ChangeLoanStatusPayload): ApiResponse<Unit>
}