package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto.AcknowledgeLoanPayload
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto.ApiLoanDetails
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto.LoanPreviewResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto.LoansResponse
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.ACCEPT_DELCINE_OFFER
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_LOAN_OFFER
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_LOAN_PREVIEWS
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.GET_PLAN_LOAN_OFFERS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface LoanService {
    @GET(GET_LOAN_PREVIEWS)
    suspend fun fetchLoanPreviews(@Query("status") status: String): ApiResponse<LoanPreviewResponse>

    @GET(GET_PLAN_LOAN_OFFERS)
    suspend fun fetchLoansBy(
        @Path("id") planId: String,
        @Query("status") status: String
    ): ApiResponse<LoansResponse>

    @GET(GET_LOAN_OFFER)
    suspend fun fetchLoan(@Path("id") loanId: String): ApiResponse<ApiLoanDetails>

    @Headers("Content-Type: application/json")
    @PATCH(ACCEPT_DELCINE_OFFER)
    suspend fun acknowledgeLoan(
        @Path("id") loanId: String,
        @Body body: AcknowledgeLoanPayload
    ): ApiResponse<Unit>
}