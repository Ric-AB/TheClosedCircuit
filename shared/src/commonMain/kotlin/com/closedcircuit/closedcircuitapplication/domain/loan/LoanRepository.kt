package com.closedcircuit.closedcircuitapplication.domain.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus

interface LoanRepository {

    suspend fun fetchLoanPreviews(loanStatus: LoanStatus): ApiResponse<List<LoanPreview>>

    suspend fun fetchLoansBy(planID: ID, loanStatus: LoanStatus): ApiResponse<List<Loan>>
}