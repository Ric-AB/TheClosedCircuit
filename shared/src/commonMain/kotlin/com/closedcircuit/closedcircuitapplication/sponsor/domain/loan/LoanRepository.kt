package com.closedcircuit.closedcircuitapplication.sponsor.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface LoanRepository {

    suspend fun fetchLoanOffers(loanStatus: LoanStatus): ApiResponse<List<LoanOffer>>
    suspend fun fetchLoanOfferDetails(id: ID): ApiResponse<LoanOfferDetails>
    suspend fun cancelLoanOffer(id: ID): ApiResponse<Unit>
}