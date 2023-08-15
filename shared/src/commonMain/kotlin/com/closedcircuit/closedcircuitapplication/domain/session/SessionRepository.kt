package com.closedcircuit.closedcircuitapplication.domain.session

interface SessionRepository {

    suspend fun updateSession(
        lastLogin: String,
        token: String,
        firebaseCustomToken: String,
        fcmServerKey: String
    )

    fun getToken(): String
}