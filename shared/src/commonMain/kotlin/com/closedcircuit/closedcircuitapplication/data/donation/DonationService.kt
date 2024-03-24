package com.closedcircuit.closedcircuitapplication.data.donation

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.donation.dto.RecentDonationsResponse
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints
import de.jensklingenberg.ktorfit.http.GET

interface DonationService {

    @GET(ClosedCircuitApiEndpoints.RECENT_DONATIONS)
    suspend fun fetchRecentDonations(): ApiResponse<RecentDonationsResponse>
}