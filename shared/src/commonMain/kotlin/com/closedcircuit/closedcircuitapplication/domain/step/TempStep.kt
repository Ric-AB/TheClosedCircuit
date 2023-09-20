package com.closedcircuit.closedcircuitapplication.domain.step

/** Temporary object to hold values used to create a persistent step item stored locally and on the server */
data class TempStep(
    val name: String,
    val description: String,
    val duration: Int,
    val targetFunds: Double,
    val planID: String,
    val userID: String
)
