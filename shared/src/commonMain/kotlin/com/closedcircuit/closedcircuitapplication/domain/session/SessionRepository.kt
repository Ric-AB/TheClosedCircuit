package com.closedcircuit.closedcircuitapplication.domain.session

import com.closedcircuit.closedcircuitapplication.domain.value.Token

interface SessionRepository {

    suspend fun get(): Session?

    suspend fun updateSession(
        lastLogin: String,
        token: String,
        firebaseCustomToken: String,
        fcmServerKey: String
    )

    fun getToken(): String
}