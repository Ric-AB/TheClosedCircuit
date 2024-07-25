package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto.AcknowledgeLoanPayload
import com.closedcircuit.closedcircuitapplication.common.domain.loan.Loan
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanDetails
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanPreview
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanSchedule
import com.closedcircuit.closedcircuitapplication.common.domain.loan.Loans
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoanRepositoryImpl(
    private val loanService: LoanService,
    private val ioDispatcher: CoroutineDispatcher
) : LoanRepository {

    override suspend fun fetchLoanPreviews(loanStatus: LoanStatus): ApiResponse<ImmutableList<LoanPreview>> {
        return withContext(ioDispatcher) {
            loanService.fetchLoanPreviews(loanStatus.name.lowercase()).mapOnSuccess { response ->
                response.previews.map {
                    val currency = Currency(it.currency)
                    LoanPreview(
                        planId = ID(it.planId),
                        planName = it.businessName,
                        totalSponsors = it.totalSponsors,
                        totalAmountOffered = Amount(it.totalAmountOffered.toDouble(), currency),
                        currency = currency,
                        sponsorAvatars = it.sponsorAvatars.map { s: String -> ImageUrl(s) }
                    )
                }.toImmutableList()
            }
        }
    }

    override suspend fun fetchLoansBy(planID: ID, loanStatus: LoanStatus): ApiResponse<Loans> {
        return withContext(ioDispatcher) {
            loanService.fetchLoansBy(planID.value, loanStatus.name.lowercase())
                .mapOnSuccess { response ->
                    response.plans.map {
                        val currency = Currency(it.currency)
                        Loan(
                            loanId = ID(it.loanOfferId),
                            sponsorAvatar = ImageUrl(it.avatar),
                            sponsorFullName = Name(it.sponsorFullName),
                            loanAmount = Amount(it.loanAmount.toDouble(), currency),
                            currency = currency,
                            gracePeriod = it.gracePeriod,
                            interestRate = it.interestRate,
                            createdAt = Date(it.createdAt)
                        )
                    }.toImmutableList()
                }
        }
    }

    override suspend fun fetchLoan(loanID: ID): ApiResponse<LoanDetails> {
        return withContext(ioDispatcher) {
            loanService.fetchLoan(loanID.value).mapOnSuccess {
                val currency = Currency(it.currency)
                LoanDetails(
                    loanID = ID(it.id),
                    interestAmount = Amount(it.interestAmount?.toDouble().orZero(), currency),
                    loanAmount = Amount(it.loanAmount?.toDouble().orZero(), currency),
                    repaymentAmount = Amount(it.repaymentAmount?.toDouble().orZero(), currency),
                    loanSchedule = it.loanSchedule?.map { apiLoanSchedule ->
                        LoanSchedule(
                            date = Date(apiLoanSchedule.date),
                            repaymentAmount = Amount(
                                apiLoanSchedule.repaymentAmount.toDouble(),
                                currency
                            ),
                            status = apiLoanSchedule.status
                        )
                    }.orEmpty(),
                    gracePeriod = it.gracePeriod ?: 0,
                    sponsorFullName = Name(it.sponsorFullName),
                    sponsorAvatar = ImageUrl(it.avatar.orEmpty()),
                    createdAt = Date(it.createdAt),
                    updatedAt = Date(it.updatedAt),
                    fundingLevel = it.fundingLevel.orEmpty(),
                    otherAmount = Amount(it.otherAmount?.toDouble().orZero(), currency),
                    currency = currency,
                    isDonation = it.isDonation ?: false,
                    status = LoanStatus.fromText(it.status.orEmpty()),
                    fundRequestID = ID(it.fundRequest.orEmpty())
                )
            }
        }
    }

    override suspend fun acknowledgeLoan(loanID: ID, status: LoanStatus): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            val payload = AcknowledgeLoanPayload(status = status.name.lowercase())
            loanService.acknowledgeLoan(loanID.value, payload)
        }
    }
}