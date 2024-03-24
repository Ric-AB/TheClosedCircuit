package com.closedcircuit.closedcircuitapplication.domain.donation

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Donations = ImmutableList<Donation>

interface DonationRepository {
    suspend fun fetchRecentDonations(): ApiResponse<Donations>
}