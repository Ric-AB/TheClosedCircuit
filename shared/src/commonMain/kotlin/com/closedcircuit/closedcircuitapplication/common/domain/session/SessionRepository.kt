package com.closedcircuit.closedcircuitapplication.beneficiary.domain.session

import com.closedcircuit.closedcircuitapplication.common.domain.session.Session

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