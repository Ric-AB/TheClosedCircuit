package com.closedcircuit.closedcircuitapplication.data.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.domain.loan.Loan
import com.closedcircuit.closedcircuitapplication.domain.loan.LoanPreview
import com.closedcircuit.closedcircuitapplication.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Amount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoanRepositoryImpl(
    private val loanService: LoanService,
    private val ioDispatcher: CoroutineDispatcher
) : LoanRepository {

    override suspend fun fetchLoanPreviews(loanStatus: LoanStatus): ApiResponse<List<LoanPreview>> {
        return withContext(ioDispatcher) {
            loanService.fetchLoanPreviews(loanStatus.name.lowercase()).mapOnSuccess { response ->
                response.previews.map {
                    LoanPreview(
                        planId = ID(it.planId),
                        planName = it.businessName,
                        totalSponsors = it.totalSponsors,
                        totalAmountOffered = Amount(it.totalAmountOffered.toDouble()),
                        sponsorAvatars = it.sponsorAvatars.map { s: String -> Avatar(s) }
                    )
                }
            }
        }
    }

    override suspend fun fetchLoansBy(planID: ID, loanStatus: LoanStatus): ApiResponse<List<Loan>> {
        return withContext(ioDispatcher) {
            loanService.fetchLoansBy(planID.value, loanStatus.name.lowercase())
                .mapOnSuccess { response ->
                    response.plans.map {
                        Loan(
                            loanId = ID(it.loanOfferId),
                            sponsorAvatar = Avatar(it.avatar),
                            sponsorFullName = Name(it.sponsorFullName),
                            loanAmount = Amount(it.loanAmount.toDouble()),
                            gracePeriod = it.gracePeriod,
                            interestRate = it.interestRate,
                            createdAt = Date(it.createdAt)
                        )
                    }
                }
        }
    }
}