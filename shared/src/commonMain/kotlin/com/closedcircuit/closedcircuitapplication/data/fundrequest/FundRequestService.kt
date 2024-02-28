package com.closedcircuit.closedcircuitapplication.data.fundrequest

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.fundrequest.dto.ApiFundRequest
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.FUND_REQUEST
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface FundRequestService {

    @Headers("Content-Type: application/json")
    @POST(FUND_REQUEST)
    suspend fun createFundRequest(@Body request: ApiFundRequest): ApiResponse<ApiFundRequest>
}