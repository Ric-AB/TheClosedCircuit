package com.closedcircuit.closedcircuitapplication.data.donation

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.domain.donation.Donation
import com.closedcircuit.closedcircuitapplication.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Amount
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                        sponsorAvatar = Avatar(it.sponsorAvatar),
                        sponsorFullName = Name(it.sponsorFullName),
                        planName = it.planName,
                        amount = Amount(it.amount.toDouble())
                    )
                }.toImmutableList()
            }
        }
    }
}