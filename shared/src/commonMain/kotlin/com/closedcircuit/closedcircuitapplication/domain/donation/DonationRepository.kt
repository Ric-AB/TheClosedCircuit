package com.closedcircuit.closedcircuitapplication.domain.donation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Donations = ImmutableList<Donation>

interface DonationRepository {
    val recentDonationsFlow: Flow<Donations>

    suspend fun getRecentDonations(): Donations
}