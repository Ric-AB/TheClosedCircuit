package com.closedcircuit.closedcircuitapplication.beneficiary.data.donation

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.Donation
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DonationRepositoryImpl(
    private val donationService: DonationService,
    private val ioDispatcher: CoroutineDispatcher
) : DonationRepository {

    override suspend fun fetchRecentDonations(): ApiResponse<Donations> {
        return withContext(ioDispatcher) {
            donationService.fetchRecentDonations().mapOnSuccess { response ->
                response.donations.map {
                    Donation(
                        id = ID(it.donationId),
                        sponsorAvatar = ImageUrl(it.sponsorAvatar),
                        sponsorFullName = Name(it.sponsorFullName),
                        planName = it.planName,
                        amount = Amount(it.amount.toDouble())
                    )
                }.toImmutableList()
            }
        }
    }
}