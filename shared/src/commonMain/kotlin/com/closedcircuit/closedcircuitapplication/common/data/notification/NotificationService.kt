package com.closedcircuit.closedcircuitapplication.beneficiary.data.notification

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.dto.ApiNotification
import com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.dto.DeleteMultipleNotificationRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.dto.GetNotificationsResponse
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.BULK_DELETE_NOTIFICATION
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.NOTIFICATIONS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface NotificationService {

    @GET(NOTIFICATIONS)
    suspend fun getNotifications(): ApiResponse<GetNotificationsResponse>

    @PATCH("${NOTIFICATIONS}mark-all-read/")
    suspend fun markAllAsRead(): ApiResponse<Unit>

    @POST("$NOTIFICATIONS{id}")
    suspend fun updateNotification(@Path("id") id: String): ApiResponse<ApiNotification>

    @DELETE("$NOTIFICATIONS{id}/")
    suspend fun deleteNotification(@Path("id") id: String): ApiResponse<Unit>

    @POST(BULK_DELETE_NOTIFICATION)
    suspend fun deleteMultipleNotifications(@Body body: DeleteMultipleNotificationRequest): ApiResponse<Unit>
}