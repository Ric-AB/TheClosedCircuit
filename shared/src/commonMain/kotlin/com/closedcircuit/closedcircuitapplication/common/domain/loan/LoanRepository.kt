package com.closedcircuit.closedcircuitapplication.common.domain.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import kotlinx.collections.immutable.ImmutableList

typealias Loans = ImmutableList<Loan>

interface LoanRepository {

    suspend fun fetchLoanPreviews(loanStatus: LoanStatus): ApiResponse<ImmutableList<LoanPreview>>

    suspend fun fetchLoansBy(planID: ID, loanStatus: LoanStatus): ApiResponse<Loans>

    suspend fun fetchLoan(loanID: ID): ApiResponse<LoanDetails>

    suspend fun acknowledgeLoan(loanID: ID, status: LoanStatus): ApiResponse<Unit>
}