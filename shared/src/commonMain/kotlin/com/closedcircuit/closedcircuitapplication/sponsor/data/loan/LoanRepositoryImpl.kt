package com.closedcircuit.closedcircuitapplication.sponsor.data.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto.ChangeLoanStatusPayload
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanOffer
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanOfferDetails
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanRepository

class LoanRepositoryImpl(
    private val loanService: LoanService
) : LoanRepository {

    override suspend fun fetchLoanOffers(loanStatus: LoanStatus): ApiResponse<List<LoanOffer>> {
        return loanService.fetchLoanOffers(loanStatus.name.lowercase()).mapOnSuccess {
            it.loans.toLoanOffers()
        }
    }

    override suspend fun fetchLoanOfferDetails(id: ID): ApiResponse<LoanOfferDetails> {
        return loanService.fetchLoanOfferDetails(id.value).mapOnSuccess {
            it.toLoanOffer()
        }
    }

    override suspend fun cancelLoanOffer(id: ID): ApiResponse<Unit> {
        val payload = ChangeLoanStatusPayload(LoanStatus.CANCELLED.value())
        return loanService.cancelLoanOffer(id.value, payload)
    }
}