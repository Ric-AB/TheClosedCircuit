package com.closedcircuit.closedcircuitapplication.domain.budget

/** Temporary object to hold values used to create a persistent budget item stored locally and on the server */
data class TempBudget(
    val name: String,
    val description: String,
    val cost: Double,
    val planID: String,
    val stepID: String
)
