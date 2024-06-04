package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavePlanPayload(
    val avatar: String = "https://cdn.pixabay.com/photo/2020/07/14/18/05/head-5405117__340.png",
    @SerialName("plan_category")
    val category: String,
    @SerialName("plan_sector")
    val sector: String,
    @SerialName("plan_type")
    val type: String,
    @SerialName("business_name")
    val name: String,
    @SerialName("plan_description")
    val description: String,
    @SerialName("plan_duration")
    val duration: Int,
    @SerialName("estimated_selling_price")
    val estimatedSellingPrice: Double,
    @SerialName("estimated_cost_price")
    val estimatedCostPrice: Double
)