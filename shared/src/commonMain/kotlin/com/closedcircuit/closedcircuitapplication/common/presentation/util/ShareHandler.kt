package com.closedcircuit.closedcircuitapplication.common.presentation.util

interface ShareHandler {
    fun sharePlanLink(text: String)

    fun buildPlanLink(fundRequestId: String): String {
       return "${Constants.PLAN_LINK_BASE_URL}$fundRequestId"
    }
}