package com.closedcircuit.closedcircuitapplication.beneficiary.data.loan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.dto.AcknowledgeLoanPayload
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.Loan
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.LoanDetails
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.LoanPreview
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.LoanSchedule
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.Loans
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
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
                    LoanPreview(
                        planId = ID(it.planId),
                        planName = it.businessName,
                        totalSponsors = it.totalSponsors,
                        totalAmountOffered = Amount(it.totalAmountOffered.toDouble()),
                        sponsorAvatars = it.sponsorAvatars.map { s: String -> Avatar(s) }
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
                        Loan(
                            loanId = ID(it.loanOfferId),
                            sponsorAvatar = Avatar(it.avatar),
                            sponsorFullName = Name(it.sponsorFullName),
                            loanAmount = Amount(it.loanAmount.toDouble()),
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
                LoanDetails(
                    loanID = ID(it.id),
                    interestAmount = Amount(it.interestAmount?.toDouble() ?: 0.0),
                    loanAmount = Amount(it.loanAmount?.toDouble() ?: 0.0),
                    repaymentAmount = Amount(it.repaymentAmount?.toDouble() ?: 0.0),
                    loanSchedule = it.loanSchedule?.map { apiLoanSchedule ->
                        LoanSchedule(
                            date = Date(apiLoanSchedule.date),
                            repaymentAmount = Amount(apiLoanSchedule.repaymentAmount.toDouble()),
                            status = apiLoanSchedule.status
                        )
                    }.orEmpty(),
                    gracePeriod = it.gracePeriod ?: 0,
                    sponsorFullName = Name(it.sponsorFullName),
                    sponsorAvatar = Avatar(it.avatar.orEmpty()),
                    createdAt = Date(it.createdAt),
                    updatedAt = Date(it.updatedAt),
                    fundingLevel = it.fundingLevel.orEmpty(),
                    otherAmount = Amount(it.otherAmount?.toDouble() ?: 0.0),
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