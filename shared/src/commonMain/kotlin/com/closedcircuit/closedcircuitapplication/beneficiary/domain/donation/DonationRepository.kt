package com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.collections.immutable.ImmutableList

typealias Donations = ImmutableList<Donation>

interface DonationRepository {
    suspend fun fetchRecentDonations(): ApiResponse<List<Donation>>
}