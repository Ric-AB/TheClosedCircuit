package com.closedcircuit.closedcircuitapplication.domain.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus

interface LoanRepository {

    suspend fun fetchLoanPreviews(loanStatus: LoanStatus): ApiResponse<List<LoanPreview>>
}