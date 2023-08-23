package com.closedcircuit.closedcircuitapplication.data.session

import com.closedcircuit.closedcircuitapplication.domain.session.Session
import com.closedcircuit.closedcircuitapplication.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.domain.value.Date
import com.closedcircuit.closedcircuitapplication.domain.value.Token
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.extensions.cached
import io.github.xxfast.kstore.utils.ExperimentalKStoreApi

class SessionRepositoryImpl(
    private val appSettingsStore: KStore<Session>
) : SessionRepository {

    override suspend fun get() = appSettingsStore.get()

    override suspend fun updateSession(
        lastLogin: String,
        token: String,
        firebaseCustomToken: String,
        fcmServerKey: String
    ) {
        val session = Session(
            lastLogin = Date(lastLogin),
            token = Token(token),
            firebaseCustomToken = Token(firebaseCustomToken),
            fcmServerKey = Token(fcmServerKey)
        )
        appSettingsStore.set(session)
        appSettingsStore.get() // allows ktore to save cached version for
    }

    @OptIn(ExperimentalKStoreApi::class)
    override fun getToken(): String = appSettingsStore.cached?.token?.value ?: ""
}