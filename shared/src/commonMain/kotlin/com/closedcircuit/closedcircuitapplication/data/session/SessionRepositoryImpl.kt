package com.closedcircuit.closedcircuitapplication.data.session

import com.closedcircuit.closedcircuitapplication.domain.session.Session
import com.closedcircuit.closedcircuitapplication.domain.session.SessionRepository
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.extensions.cached
import io.github.xxfast.kstore.utils.ExperimentalKStoreApi

class SessionRepositoryImpl(
    private val appSettingsStore: KStore<Session>
) : SessionRepository {

    override suspend fun updateSession(
        lastLogin: String,
        token: String,
        firebaseCustomToken: String,
        fcmServerKey: String
    ) {
        appSettingsStore.set(Session(lastLogin, token, firebaseCustomToken, fcmServerKey))
        appSettingsStore.get() // allows ktore to save cached version for
    }

    @OptIn(ExperimentalKStoreApi::class)
    override fun getToken(): String = appSettingsStore.cached?.token ?: ""
}