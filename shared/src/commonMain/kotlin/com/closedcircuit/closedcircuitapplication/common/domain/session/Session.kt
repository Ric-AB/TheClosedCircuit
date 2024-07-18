package com.closedcircuit.closedcircuitapplication.common.domain.session

import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.Token
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val lastLogin: Date,
    val token: Token,
    val firebaseCustomToken: Token,
    val fcmServerKey: Token
) {

    companion object {
        const val SESSION_DURATION_IN_HOURS = 6
    }

    fun hasExpired(firebaseUser: FirebaseUser?): Boolean {
        val lastLoginAsLocalDateTime = lastLogin.toInstant()
        val now = Clock.System.now()
        val differenceInHours = now - lastLoginAsLocalDateTime
        return differenceInHours.inWholeHours >= SESSION_DURATION_IN_HOURS || firebaseUser == null
    }
}
