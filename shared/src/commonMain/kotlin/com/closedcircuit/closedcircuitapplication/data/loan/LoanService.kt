package com.closedcircuit.closedcircuitapplication.data.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.loan.dto.LoanPreviewResponse
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.GET_LOAN_PREVIEWS
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface LoanService {
    @GET(GET_LOAN_PREVIEWS)
    suspend fun fetchLoanPreviews(@Query("status") status: String): ApiResponse<LoanPreviewResponse>
}